package com.example.chatterbox.chat.users.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.chat.users.data.FirestoreUserRepository
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.domain.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    val TAG = "UserViewModel"
    
    private val _user = MutableStateFlow<UserState>(UserState())
    val user: StateFlow<UserState> = _user

    private var _loadState = MutableStateFlow<LoadState>(LoadState.Idle)
    val loadState = _loadState

    init {
        Log.d(TAG, "ViewModel created")
        _loadState.value = LoadState.Idle
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {

            _user.update {
                it.copy(
                    isLoading = true
                )
            }

            val profile = userRepository.getCurrentUserProfile()
            Log.d(TAG, "getCurrentUser: $profile")
            _user.update {
                _user.value.copy(
                    user = profile,
                    isLoading = false
                )
            }
            Log.d(TAG, "getCurrentUser: ${_user.value}")
        }
    }

    fun updateCurrentUser(user: User){
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            userRepository.updateUserProfile(user)
            Log.d(TAG, "updateCurrentUser: $user")
//            _user.value = _user.value.copy(user = user, isLoading = false)
            getCurrentUser()
            _loadState.value = LoadState.Success
        }
    }

}

sealed class LoadState() {
    object Idle: LoadState()
    object Loading: LoadState()
    object Success: LoadState()
    data class Error(val exception: Throwable?): LoadState()
}