package com.example.chatterbox.chat.users.domain

import com.example.chatterbox.chat.users.domain.User

interface UserRepository {
    suspend fun createUserProfileIfNotExists(user: User)
    suspend fun getCurrentUserProfile(): User?
    suspend fun updateUserProfile(user: User)
}