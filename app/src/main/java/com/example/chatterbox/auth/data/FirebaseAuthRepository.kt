package com.example.chatterbox.auth.data

import android.util.Log
import com.example.chatterbox.auth.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
): AuthRepository {
    val TAG = "FirebaseAuthRepository"

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser


    override suspend fun signInWithGoogle(idToken: String): Result<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            val userId = firebaseAuth.currentUser?.uid ?: return Result.failure(IllegalStateException("User is null after sign in"))
            return Result.success(userId)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

}