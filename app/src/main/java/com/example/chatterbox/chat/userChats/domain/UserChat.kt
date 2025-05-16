package com.example.chatterbox.chat.userChats.domain

data class UserChat(
    val id: String = "",
    val members: MutableList<Member> = mutableListOf(),
    val memberIds: MutableList<String> = mutableListOf(),
    val lastMessageTime: Long = System.currentTimeMillis(),
    val lastMessageUserId: String = "",
    val lastMessageUsername: String = "",
    val lastMessage: String = "",
)
