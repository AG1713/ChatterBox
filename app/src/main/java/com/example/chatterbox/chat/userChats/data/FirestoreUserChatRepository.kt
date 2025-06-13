package com.example.chatterbox.chat.userChats.data

import android.util.Log
import com.example.chatterbox.chat.shared.domain.Member
import com.example.chatterbox.chat.shared.domain.Message
import com.example.chatterbox.chat.userChats.domain.UserChat
import com.example.chatterbox.chat.userChats.domain.UserChatRepository
import com.example.chatterbox.core.common.FirestoreCollections
import com.example.chatterbox.core.common.ListenerRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class FirestoreUserChatRepository (
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
): UserChatRepository {

    private val TAG = "FirestoreUserChatRepository"

    private val currentUserId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    private val _userChats = MutableStateFlow<List<UserChat>>(listOf())
    override val userChats = _userChats.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(listOf())
    override val messages = _messages.asStateFlow()

    override fun getAllUserChats(): StateFlow<List<UserChat>> {

        val userChatCollection = firestore.collection(FirestoreCollections.USERCHATS)
        Log.d(TAG, "getAllUserChatsForUser: Inside function")

        ListenerRegistry.register(ListenerRegistry.ListenerKeys.USER_CHATS_LISTENER,
            userChatCollection
                .whereArrayContains("memberIds", currentUserId!!)
                .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                .addSnapshotListener {
                        snapshot, error ->
                    Log.d(TAG, "getAllUserChatsForUser: Inside snapshot listener")

                    if (error != null || snapshot == null) {
                        Log.e(TAG, "getAllUserChatsForUser: $error")
                        return@addSnapshotListener
                    }

                    val userChats = snapshot.documents.mapNotNull {
                            doc ->
                        doc.toObject(UserChat::class.java)?.copy(id = doc.id)
                    }

                    _userChats.value = userChats
                }
            )

        return userChats
    }

    override fun clearUserChatsListeners(){
        ListenerRegistry.remove(ListenerRegistry.ListenerKeys.USER_CHATS_LISTENER)
    }

    override fun clearChatsListener(){
        ListenerRegistry.remove(ListenerRegistry.ListenerKeys.CHATS_LISTENER)
    }

    override suspend fun getOrCreateUserChat(otherId: String, otherUsername: String, currentId: String, currentUsername: String): String {
        val userChatsCollection = firestore.collection(FirestoreCollections.USERCHATS)
        val userChatId = if (otherId > currentId) "${otherId}_${currentId}" else "${currentId}_${otherId}"

        val userChat = userChatsCollection.document(userChatId).get().await().toObject(UserChat::class.java)

        if (userChat == null){
            userChatsCollection.document(userChatId).set(
                UserChat(
                    id = userChatId,
                    memberIds = mutableListOf(currentId, otherId),
                    members = mutableListOf(Member(currentId, currentUsername), Member(otherId, otherUsername)),
                    lastMessageTime = System.currentTimeMillis(),
                    lastMessageUserId = currentId,
                    lastMessageUsername = currentUsername,
                    lastMessage = "Started chat with you!"
                )
            ).await()
        }
        return userChatId
    }

    override fun getAllMessages(chatRoomId: String): StateFlow<List<Message>> {
        val messagesCollection = firestore
            .collection(FirestoreCollections.USERCHATS)
            .document(chatRoomId)
            .collection(FirestoreCollections.CHATS)

        ListenerRegistry.register(ListenerRegistry.ListenerKeys.CHATS_LISTENER,
            messagesCollection
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

                })

        return messages
    }

    override fun clearMessagesStateFlow(){
        _messages.value = listOf<Message>()
    }

    override fun sendMessage(chatRoomId: String, senderUsername: String, text: String){
        val userChatDocument = firestore
            .collection(FirestoreCollections.USERCHATS)
            .document(chatRoomId)

        val messagesCollection = userChatDocument
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

        userChatDocument.update(
            "lastMessage", text,
            "lastMessageUserId", currentUserId!!,
            "lastMessageUsername", senderUsername,
            "lastMessageTime", time
        )

    }

}