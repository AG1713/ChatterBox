package com.example.chatterbox.chat.groups.domain

import com.example.chatterbox.chat.shared.domain.Member
import com.example.chatterbox.chat.shared.domain.Message
import kotlinx.coroutines.flow.StateFlow

interface GroupRepository {
    val groups: StateFlow<List<Group>>
    val messages: StateFlow<List<Message>>
    fun getAllGroups(): StateFlow<List<Group>>
    fun clearListeners()
    suspend fun createGroup(
        currentUsername: String,
        name: String,
        photoUrl: String,
        description: String
    ): String
    fun addMembers(groupId: String, memberIds: List<String>, members: List<Member>)
    fun leaveGroup(groupId: String, userId: String, username: String)
    fun getAllMessages(groupId: String): StateFlow<List<Message>>
    fun clearMessagesStateFlow()
    fun clearChatsListener()
    fun sendMessage(groupId: String, senderUsername: String, text: String)
    suspend fun getGroup(groupId: String): Group?
}