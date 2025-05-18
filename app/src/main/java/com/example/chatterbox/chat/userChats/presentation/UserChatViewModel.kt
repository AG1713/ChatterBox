package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.chat.userChats.domain.UserChatRepository
import com.example.chatterbox.chat.users.domain.UserRepository
import kotlinx.coroutines.launch

class UserChatViewModel(
    private val userChatRepository: UserChatRepository,
    private val userRepository: UserRepository
): ViewModel() {
    val TAG = "UserChatViewModel"

//    init {
//        viewModelScope.launch {
//            userChatRepository.getAllUserChats()
//        }
//    }

    val userChats = userChatRepository.userChats

    fun getAllUserChats(){
        viewModelScope.launch {
            userChatRepository.getAllUserChats()
            Log.d(TAG, "getAllUserChats: userChatListener activated")
        }
    }

    fun clearUserChatsListeners(){
        userChatRepository.clearAllListeners()
        Log.d(TAG, "getAllUserChats: userChatListener removed")
    }

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