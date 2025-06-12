package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.chat.userChats.domain.Message
import com.example.chatterbox.chat.userChats.domain.UserChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel (
    private val userChatRepository: UserChatRepository
): ViewModel() {
    private val TAG = "ChatViewModel"

    val messages = userChatRepository.messages

    fun getAllMessages(chatRoomId: String) {
        viewModelScope.launch {
            userChatRepository.getAllMessages(chatRoomId)
            Log.d(TAG, "getAllMessages: chats listener activated")
        }
    }

    fun sendMessage(chatRoomId: String, text: String, senderUsername: String){
        viewModelScope.launch {
            userChatRepository.sendMessage(chatRoomId = chatRoomId, text = text, senderUsername = senderUsername)
        }
    }

    fun exitChat(){
        userChatRepository.clearChatsListener()
        Log.d(TAG, "clearChatsListener: chats listener removed")
        userChatRepository.clearMessagesStateFlow()
    }

}