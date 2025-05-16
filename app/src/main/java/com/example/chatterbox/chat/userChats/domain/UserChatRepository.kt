package com.example.chatterbox.chat.userChats.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UserChatRepository {
    val userChats: StateFlow<List<UserChat>>
    suspend fun getAllUserChatsForUser(id: String): MutableStateFlow<List<UserChat>>;
    suspend fun getOrCreateUserChat(id: String);
}