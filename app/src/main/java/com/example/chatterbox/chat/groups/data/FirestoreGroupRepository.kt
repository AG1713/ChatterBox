package com.example.chatterbox.chat.groups.data

import android.util.Log
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.groups.domain.GroupRepository
import com.example.chatterbox.chat.shared.domain.Member
import com.example.chatterbox.chat.shared.domain.Message
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.core.common.FirestoreCollections
import com.example.chatterbox.core.common.ListenerRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.random.Random

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
                        doc.toObject(Group::class.java)
                    }

                    _groups.value = groupsList
                }
        )
        return groups
    }

    override fun clearListeners(){
        ListenerRegistry.remove(ListenerRegistry.ListenerKeys.GROUPS_LISTENER)
    }

    override suspend fun createGroup(currentUsername: String, name: String, photoUrl: String, description: String): String {
        // For now, we assume the creator is the only member
        val groupsCollection = firestore.collection(FirestoreCollections.GROUPS)

        val groupId = UUID.randomUUID().toString()
        groupsCollection.document(groupId).set(
            Group(
                id = groupId,
                name = name,
                groupPhotoUrl = "",
                description = description,
                creationTimestamp = System.currentTimeMillis().toString(),
                memberIds = listOf(currentUserId!!),
                members = listOf(Member(currentUserId!!, currentUsername)),
                lastMessage = "Created a group!",
                lastMessageUserId = currentUserId!!,
                lastMessageUsername = currentUsername,
                lastMessageTime = System.currentTimeMillis()
            )
        ).await()

        return groupId
    }

    override fun addMembers(groupId: String, memberIds: List<String>, members: List<Member>) {
        // Intentionally kept to accept a list to expand in future later
        val group = firestore.collection(FirestoreCollections.GROUPS).document(groupId)
        group.update("memberIds", FieldValue.arrayUnion(memberIds))
        group.update("members", FieldValue.arrayUnion(members))
    }

    override fun leaveGroup(groupId: String, userId: String, username: String) {
        val group = firestore.collection(FirestoreCollections.GROUPS).document(groupId)
        group.update("memberIds", FieldValue.arrayUnion(userId))
        group.update("members", FieldValue.arrayUnion(Member(userId, username)))
    }

    override fun getAllMessages(groupId: String): StateFlow<List<Message>> {
        val groupDocument = firestore.collection(FirestoreCollections.GROUPS).document(groupId)

        ListenerRegistry.register(
            ListenerRegistry.ListenerKeys.GROUP_CHATS_LISTENER,
            groupDocument.collection(FirestoreCollections.CHATS)
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null || snapshot == null) {
                        Log.e(TAG, "getAllMessages: $error")
                    }
                    else {
                        _messages.value = snapshot.documents.mapNotNull {
                            it.toObject(Message::class.java)
                        }
                    }
                }
        )

        return messages
    }

    override fun sendMessage(groupId: String, senderUsername: String, text: String){
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

    }

    override fun clearMessagesStateFlow(){
        _messages.value = listOf()
    }

    override fun clearChatsListener() {
        ListenerRegistry.remove(ListenerRegistry.ListenerKeys.GROUP_CHATS_LISTENER)
    }


}