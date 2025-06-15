package com.example.chatterbox.chat.users.data

import android.util.Log
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.domain.UserRepository
import com.example.chatterbox.core.common.FirestoreCollections
import com.example.chatterbox.core.common.ListenerRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository (
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
): UserRepository {
    val TAG = "FirestoreUserRepository"
    val currentUserId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    private val _users = MutableStateFlow<List<User>>(listOf())
    override val users = _users.asStateFlow()

    override suspend fun createUserProfileIfNotExists(user: User) {
        // We have passed user object here in hopes that later after first login, we will
        // make the user to set his profile details before going to main screen
        val userDoc = firestore.collection(FirestoreCollections.USERS).document(user.id)
        val snapshot = userDoc.get().await()

        if (!snapshot.exists()){
            userDoc.set(user)
        }

    }

    override suspend fun getCurrentUserProfile(): User? {
        return firestore.collection(FirestoreCollections.USERS).document(currentUserId!!).get(Source.SERVER)
            .await().toObject<User>()
    }

    override suspend fun getUserProfile(id: String): User? {
        return try {
            firestore.collection(FirestoreCollections.USERS).document(id).get()
                .await().toObject<User>()
        }
        catch (e: Exception){
            Log.d(TAG, "getUserProfile: ${e.message}")
            null
        }
    }

    override suspend fun updateUserProfile(user: User) {
        try {
            if (user.id != currentUserId){
                // Just a basic check
                return
            }
            val userDoc = firestore.collection(FirestoreCollections.USERS).document(currentUserId!!)

            userDoc.set(user)
        }
        catch (e: Exception){
            Log.d("EXCEPTION", "culprit: ")
        }

    }

    override suspend fun searchUsers(hint: String): MutableStateFlow<List<User>> {
        if (hint.isEmpty()) {
            _users.value = listOf()
            return _users
        }

        try {
            val snapshot = firestore.collection(FirestoreCollections.USERS)
                .whereGreaterThanOrEqualTo("username", hint)
                .whereLessThan("username", hint + "\uf8ff")
                .whereNotEqualTo("id", currentUserId)
                .orderBy("username")
                .get()
                .await()

            val users = snapshot.documents.mapNotNull { doc ->
                doc.toObject(User::class.java)
            }

            _users.value = users
        }
        catch (e: Exception){
            Log.e(TAG, "searchUsers: ${e.message}", )
        }

        return _users
    }


}