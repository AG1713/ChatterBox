package com.example.chatterbox.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.auth.domain.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState
    private val _user = MutableStateFlow<FirebaseUser?>(authRepository.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithGoogle(idToken)
            if (result.isSuccess) {
                _user.value = authRepository.currentUser
                _authState.value = AuthState.Success
            } else {
                AuthState.Error(result.exceptionOrNull())
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }

}

sealed class AuthState() {
    object Idle: AuthState()
    object Loading: AuthState()
    object Success: AuthState()
    data class Error(val exception: Throwable?): AuthState()
}