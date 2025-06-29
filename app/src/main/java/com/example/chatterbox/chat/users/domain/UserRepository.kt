package com.example.chatterbox.chat.users.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val users: StateFlow<List<User>>

    suspend fun createUserProfileIfNotExists(user: User)
    suspend fun getCurrentUserProfile(): User?
    suspend fun getUserProfile(id: String): User?
    suspend fun updateUserProfile(user: User)
    suspend fun searchUsers(hint: String): MutableStateFlow<List<User>>
//    suspend fun updateMessageToken(userId: String, messageToken: String)
}