package com.example.chatterbox.chat.userChats.data

import android.util.Log
import com.example.chatterbox.chat.userChats.domain.UserChat
import com.example.chatterbox.chat.userChats.domain.UserChatRepository
import com.example.chatterbox.core.common.FirestoreCollections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow

class FirestoreUserChatRepository (
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
): UserChatRepository {

    private val TAG = "FirestoreUserChatRepository"

    val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid


    private val _userChats = MutableStateFlow<List<UserChat>>(listOf())
    override val userChats = _userChats

    override suspend fun getAllUserChatsForUser(id: String): MutableStateFlow<List<UserChat>> {

        val userChatCollection = firestore.collection(FirestoreCollections.USERCHATS)
        Log.d(TAG, "getAllUserChatsForUser: Inside function")

        userChatCollection
            .whereArrayContains("memberIds", currentUserId)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
            .addSnapshotListener {
                snapshot, error ->
                Log.d(TAG, "getAllUserChatsForUser: Inside snapshot listener")

                if (error != null || snapshot == null) {
                    Log.e(TAG, "getAllUserChatsForUser: $error")
                }

                val userChats = snapshot!!.documents.mapNotNull {
                    doc ->
                    doc.toObject(UserChat::class.java)?.copy(id = doc.id)
                }

                _userChats.value = userChats
            }

        return _userChats
    }

    override suspend fun getOrCreateUserChat(id: String) {
        TODO("Not yet implemented")
    }
}