package com.example.chatterbox.chat.userChats.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UserChatRepository {
    val userChats: StateFlow<List<UserChat>>
    val messages: StateFlow<List<Message>>

    fun getAllUserChats(): StateFlow<List<UserChat>>
    fun clearUserChatsListeners()
    suspend fun getOrCreateUserChat(
        otherId: String,
        otherUsername: String,
        currentId: String,
        currentUsername: String
    ): String
    fun getAllMessages(chatRoomId: String): StateFlow<List<Message>>
    fun sendMessage(chatRoomId: String, senderUsername: String, text: String)
    fun clearChatsListener()
    fun clearMessagesStateFlow()
}