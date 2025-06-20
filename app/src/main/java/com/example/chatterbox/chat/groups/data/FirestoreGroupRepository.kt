package com.example.chatterbox.chat.groups.data

import android.net.Uri
import android.util.Log
import com.example.chatterbox.BuildConfig
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.groups.domain.GroupRepository
import com.example.chatterbox.chat.shared.domain.Member
import com.example.chatterbox.chat.shared.domain.Message
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.core.common.FirestoreCollections
import com.example.chatterbox.core.common.ListenerRegistry
import com.example.chatterbox.core.common.SupabaseBuckets
import com.example.chatterbox.retrofit.fcm.FCMService
import com.example.chatterbox.retrofit.token.sendNotificationWithFetchedToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.context.GlobalContext
import java.util.UUID

class FirestoreGroupRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
): GroupRepository {

    private val TAG = "FirestoreGroupRepository"

    private val currentUserId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    private val _groups = MutableStateFlow<List<Group>>(listOf())
    override val groups = _groups.asStateFlow()
    private val _messages = MutableStateFlow<List<Message>>(listOf())
    override val messages = _messages.asStateFlow()

    override fun getAllGroups(): StateFlow<List<Group>>{
        val groupsCollection = firestore.collection(FirestoreCollections.GROUPS)

        ListenerRegistry.register(
            ListenerRegistry.ListenerKeys.GROUPS_LISTENER,
            groupsCollection.whereArrayContains("memberIds", currentUserId!!)
                .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                .addSnapshotListener {
                    snapshot, error ->

                    if (error != null || snapshot == null) {
                        Log.e(TAG, "getAllUserChatsForUser: $error")
                        return@addSnapshotListener
                    }

                    val groupsList = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(Group::class.java)
                        } catch (e: Exception) {
                            Log.e("DeserializationError", "Failed doc id=${doc.id}, data=${doc.data}, error=${e.message}")
                            null
                        }
                    }

                    _groups.value = groupsList
                }
        )
        return groups
    }

    override suspend fun getGroup(groupId: String): Group? {
        return firestore.collection(FirestoreCollections.GROUPS)
            .document(groupId).get().await().toObject(Group::class.java)
    }

    override fun clearListeners(){
        ListenerRegistry.remove(ListenerRegistry.ListenerKeys.GROUPS_LISTENER)
    }

    override suspend fun createGroup(currentUsername: String, name: String, description: String): String {
        // For now, we assume the creator is the only member
        try {
            val groupsCollection = firestore.collection(FirestoreCollections.GROUPS)

            val groupId = UUID.randomUUID().toString()
            val group = Group(
                id = groupId,
                name = name,
                groupPhotoUrl = "${BuildConfig.SUPABASE_URL}storage/v1/object/public/${SupabaseBuckets.GROUP_PHOTOS}/${groupId}.jpg",
                description = description,
                creationTimestamp = System.currentTimeMillis(),
                memberIds = listOf(currentUserId!!),
                members = listOf(Member(currentUserId!!, currentUsername)),
                lastMessage = "Created a group!",
                lastMessageUserId = currentUserId!!,
                lastMessageUsername = currentUsername,
                lastMessageTime = System.currentTimeMillis()
            )
            groupsCollection.document(groupId).set(group).await()

            Log.d(TAG, "createGroup: $group")

            return groupId
        }
        catch (e: Exception) {
            Log.d(TAG, "createGroup: Error creating group: ${e.message}")
        }
        return ""
    }

    override fun updateGroup(group: Group) {
        try {
            val groupDoc = firestore.collection(FirestoreCollections.GROUPS).document(group.id)
            groupDoc.set(group)
        }
        catch (e: Exception){
            Log.d(TAG, "Error updating group: ${e.message}")
        }
    }

    override fun addMembers(groupId: String, memberIds: List<String>, members: List<Member>) {
        // Intentionally kept to accept a list to expand in future later
        /*
        the spread operator is *, and it's used to pass an array (or array-like collection)
        into a function that expects vararg parameters (i.e., a variable number of arguments).
        */
        try {
            val group = firestore.collection(FirestoreCollections.GROUPS).document(groupId)
            group.update("memberIds", FieldValue.arrayUnion(*memberIds.toTypedArray()))
            group.update("members", FieldValue.arrayUnion(*members.toTypedArray()))
        }
        catch (e: Exception){
            Log.d(TAG, "Error adding members: ${e.message}")
        }
    }

    override fun leaveGroup(groupId: String, userId: String, username: String) {
        try {
            val group = firestore.collection(FirestoreCollections.GROUPS).document(groupId)

            val batch = firestore.batch()
            batch.update(group, "memberIds", FieldValue.arrayRemove(userId))
            batch.update(group, "members", FieldValue.arrayRemove(Member(userId, username)))
            batch.commit().addOnSuccessListener {
                group.get().addOnSuccessListener { snapshot ->
                    val groupObj = snapshot.toObject(Group::class.java)
                    val members = groupObj?.members
                    if (members.isNullOrEmpty()) group.delete()
                }
                    .addOnFailureListener {
                        Log.e(
                            "LeaveGroup",
                            "Failed to check group: ${it.message}"
                        )
                    }
            }
                .addOnFailureListener {
                    Log.e(
                        "LeaveGroup",
                        "Failed to update group: ${it.message}"
                    )
                }
        }
        catch (e: Exception) {
            Log.e(TAG, "leaveGroup: Error leaving group- ${e.message}", )
        }

    }

    override fun getAllMessages(groupId: String): StateFlow<List<Message>> {
        try {
            val groupDocument = firestore.collection(FirestoreCollections.GROUPS).document(groupId)

            ListenerRegistry.register(
                ListenerRegistry.ListenerKeys.GROUP_CHATS_LISTENER,
                groupDocument.collection(FirestoreCollections.CHATS)
                    .orderBy("time", Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null || snapshot == null) {
                            Log.e(TAG, "getAllMessages: $error")
                        } else {
                            _messages.value = snapshot.documents.mapNotNull {
                                it.toObject(Message::class.java)
                            }
                        }
                    }
            )
        }
        catch (e: Exception) {
            Log.e(TAG, "getAllMessages: Error fetching messages- ${e.message}", )
        }

        return messages
    }

    override fun sendMessage(groupId: String, senderUsername: String, text: String){
        try {
            val groupDocument = firestore
                .collection(FirestoreCollections.GROUPS)
                .document(groupId)

            val messagesCollection = groupDocument
                .collection(FirestoreCollections.CHATS)

            val time = System.currentTimeMillis()

            messagesCollection.add(
                Message(
                    text = text,
                    senderId = currentUserId!!,
                    senderUsername = senderUsername,
                    time = time
                )
            )

            groupDocument.update(
                "lastMessage", text,
                "lastMessageUserId", currentUserId!!,
                "lastMessageUsername", senderUsername,
                "lastMessageTime", time
            )

            CoroutineScope(Dispatchers.IO).launch {
                val fcmService: FCMService = GlobalContext.get().get() // Somehow getting from Koin
                val memberIds = groupDocument.get().await().toObject<Group>()?.memberIds
                Log.d(TAG, "sendMessage: Sending notifications")

                if (!memberIds.isNullOrEmpty()) {
                    memberIds.forEach { memberId ->
                        if (memberId != currentUserId) {
                            val userToken =
                                firestore.collection(FirestoreCollections.USERS).document(memberId)
                                    .get().await().toObject<User>()!!.messageToken
                            sendNotificationWithFetchedToken(
                                userFcmToken = userToken,
                                title = senderUsername,
                                message = text,
                                fcmService = fcmService
                            )
                        }
                    }
                }

            }
        }
        catch (e: Exception) {
            Log.e(TAG, "sendMessage: Error sending message- ${e.message}", )
        }

    }

    override fun clearMessagesStateFlow(){
        _messages.value = listOf()
    }

    override fun clearChatsListener() {
        ListenerRegistry.remove(ListenerRegistry.ListenerKeys.GROUP_CHATS_LISTENER)
    }


}