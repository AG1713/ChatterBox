package com.example.chatterbox.chat.users.domain

import com.example.chatterbox.chat.userChats.domain.Message
import com.example.chatterbox.chat.users.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val users: StateFlow<List<User>>

    suspend fun createUserProfileIfNotExists(user: User)
    suspend fun getCurrentUserProfile(): User?
    suspend fun getUserProfile(id: String): User?
    suspend fun updateUserProfile(user: User)
    suspend fun searchUsers(hint: String): MutableStateFlow<List<User>>
}