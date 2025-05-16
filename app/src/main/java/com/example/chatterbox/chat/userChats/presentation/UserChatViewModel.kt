package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.chat.userChats.domain.UserChatRepository
import com.example.chatterbox.chat.users.domain.UserRepository
import com.example.chatterbox.core.common.currentUserId
import kotlinx.coroutines.launch

class UserChatViewModel(
    private val userChatRepository: UserChatRepository,
    private val userRepository: UserRepository
): ViewModel() {
    val TAG = "UserChatViewModel"

    init {
        viewModelScope.launch {
            userChatRepository.getAllUserChatsForUser(currentUserId)
        }

    }

    val userChats = userChatRepository.userChats

    suspend fun getUsername(id: String): String? {
        Log.d(TAG, "getUsername: Called")
        val user = userRepository.getUserProfile(id)
        return user?.username
    }

    fun getOrCreateUserChat(id: String){
        viewModelScope.launch {
            userChatRepository.getOrCreateUserChat(id)
        }
    }

}