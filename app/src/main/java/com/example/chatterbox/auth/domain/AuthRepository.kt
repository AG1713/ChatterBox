package com.example.chatterbox.auth.domain

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun signInWithGoogle(idToken: String): Result<String>
    fun signOut()
}