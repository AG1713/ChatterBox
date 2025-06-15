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
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _otherUser = MutableStateFlow<User?>(null)
    val otherUser: StateFlow<User?> = _otherUser

    private var _loadState = MutableStateFlow<LoadState>(LoadState.Idle)
    val loadState = _loadState

    init {
        Log.d(TAG, "UserViewModel created")
        _loadState.value = LoadState.Idle
    }

    fun resetLoadState(){
        _loadState.value = LoadState.Idle
    }

    fun getCurrentUser() {
        viewModelScope.launch {

            try {
                _loadState.value = LoadState.Loading
                val profile = userRepository.getCurrentUserProfile()
                Log.d(TAG, "getCurrentUser: $profile")
                _user.value = profile
                Log.d(TAG, "getCurrentUser: ${_user.value}")
                _loadState.value = LoadState.Idle
            }
            catch (e: NullPointerException){
                Log.d(TAG, "culprit: $e")
            }
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            _otherUser.value = userRepository.getUserProfile(userId)
            _loadState.value = LoadState.Idle
        }
    }

    fun clearOtherUser(){
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            _otherUser.value = null
            _loadState.value = LoadState.Idle
        }
    }

    fun updateCurrentUser(user: User) {
        Log.d(TAG, "Entered updateCurrentUser")

        viewModelScope.launch {
            Log.d(TAG, "Inside launch")

            try {
                _loadState.value = LoadState.Loading
                Log.d(TAG, "Before updateUserProfile")

                userRepository.updateUserProfile(user)

                Log.d(TAG, "After updateUserProfile")

                getCurrentUser()

                _loadState.value = LoadState.Success
            } catch (e: Exception) {
                Log.e(TAG, "Error in updateCurrentUser", e)
            }
        }
    }


}

sealed class LoadState() {
    object Idle: LoadState()
    object Loading: LoadState()
    object Success: LoadState()
    data class Error(val exception: Throwable?): LoadState()
}