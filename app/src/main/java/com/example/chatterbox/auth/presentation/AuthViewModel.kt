package com.example.chatterbox.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.BuildConfig
import com.example.chatterbox.auth.domain.AuthRepository
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.domain.UserRepository
import com.example.chatterbox.core.common.SupabaseBuckets
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState
    private val _navigateToChat = MutableSharedFlow<Unit>()
    val navigateToChat = _navigateToChat.asSharedFlow()
    private val _user = MutableStateFlow<FirebaseUser?>(authRepository.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    suspend fun signInWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading
        val result = authRepository.signInWithGoogle(idToken)
        if (result.isSuccess) {
            _user.value = authRepository.currentUser
            val messageToken = Firebase.messaging.token.await()
            userRepository.createUserProfileIfNotExists(
                User(
                    id = _user.value!!.uid, // Assuming it is not null
                    username = "User",
                    email = _user.value!!.email!!,
                    description = "Default description",
                    profilePhotoUrl = "${BuildConfig.SUPABASE_URL}storage/v1/object/public/${SupabaseBuckets.USER_PHOTOS}/${_user.value!!.uid}.jpg",
                    lastActive = System.currentTimeMillis(),
                    dateCreated = System.currentTimeMillis(),
                    messageToken = messageToken
                )
            )

            _authState.value = AuthState.Success
            _navigateToChat.emit(Unit)
        } else {
            AuthState.Error(result.exceptionOrNull())
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _user.value = null
            _authState.value = AuthState.Idle
        }
    }

}

sealed class AuthState() {
    object Idle: AuthState()
    object Loading: AuthState()
    object Success: AuthState()
    data class Error(val exception: Throwable?): AuthState()
}