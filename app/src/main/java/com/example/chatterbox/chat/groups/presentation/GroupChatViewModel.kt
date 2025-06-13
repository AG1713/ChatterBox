package com.example.chatterbox.chat.groups.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.chat.groups.domain.GroupRepository
import kotlinx.coroutines.launch

class GroupChatViewModel(
    private val groupRepository: GroupRepository
): ViewModel() {
    private val TAG = "GroupChatViewModel"

    val messages = groupRepository.messages

    fun getAllMessages(groupId: String) {
        viewModelScope.launch {
            groupRepository.getAllMessages(groupId)
            Log.d(TAG, "getAllMessages: chats listener activated")
        }
    }

    fun sendMessage(groupId: String, text: String, senderUsername: String){
        viewModelScope.launch {
            groupRepository.sendMessage(groupId = groupId, text = text, senderUsername = senderUsername)
        }
    }

    fun exitChat(){
        groupRepository.clearChatsListener()
        Log.d(TAG, "clearChatsListener: chats listener removed")
        groupRepository.clearMessagesStateFlow()
    }

}