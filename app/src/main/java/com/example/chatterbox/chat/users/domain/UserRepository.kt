package com.example.chatterbox.chat.users.domain

import com.example.chatterbox.chat.users.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    suspend fun createUserProfileIfNotExists(user: User)
    suspend fun getCurrentUserProfile(): User?
    suspend fun getUserProfile(id: String): User?
    suspend fun updateUserProfile(user: User)
    suspend fun searchUsers(hint: String): MutableStateFlow<List<User>>
    val users: StateFlow<List<User>>
}