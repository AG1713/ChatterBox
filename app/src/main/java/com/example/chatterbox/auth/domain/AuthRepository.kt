package com.example.chatterbox.auth.domain

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
}