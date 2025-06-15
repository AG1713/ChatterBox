package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.chat.shared.domain.LoadState
import com.example.chatterbox.chat.userChats.domain.UserChatRepository
import com.example.chatterbox.chat.users.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserChatViewModel(
    private val userChatRepository: UserChatRepository,
    private val userRepository: UserRepository
): ViewModel() {
    val TAG = "UserChatViewModel"

    val userChats = userChatRepository.userChats
    val users = userRepository.users

    private val _loadState = MutableStateFlow<LoadState>(LoadState.Idle)
    val loadState: StateFlow<LoadState> = _loadState

    init {
        Log.d(TAG, "UserChatViewModel created")
    }

    fun getAllUserChats(){
        viewModelScope.launch {
            userChatRepository.getAllUserChats()
            Log.d(TAG, "getAllUserChats: userChatListener activated")
        }
    }

    fun getAllUsersWithHint(hint: String) {
        viewModelScope.launch {
            try {
                userRepository.searchUsers(hint)
            }
            catch (e: Exception){
                Log.e(TAG, "getAllUsersWithHint: ${e.message}")
            }
        }
    }

    fun clearUserChatsListeners(){
        userChatRepository.clearUserChatsListeners()
        Log.d(TAG, "getAllUserChats: userChatListener removed")
    }

    suspend fun getUsername(id: String): String? {
        Log.d(TAG, "getUsername: Called")
        val user = userRepository.getUserProfile(id)
        return user?.username
    }

    fun getOrCreateUserChat(otherId: String, otherUsername: String, currentId: String, currentUsername: String,
                            onComplete: (chatRoomId: String) -> Unit){
        viewModelScope.launch {
            val chatRoomId = userChatRepository.getOrCreateUserChat(
                otherId = otherId,
                otherUsername = otherUsername,
                currentId = currentId,
                currentUsername = currentUsername
            )
            onComplete(chatRoomId)
        }
    }

    fun deleteUserChat(userChatId: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            userChatRepository.deleteUserChat(userChatId)
            onComplete()
            _loadState.value = LoadState.Idle
        }
    }

}