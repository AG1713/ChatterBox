package com.example.chatterbox.chat.shared.domain

data class Message(
    val text: String = "",
    val time: Long = System.currentTimeMillis(),
    val senderId: String = "",
    val senderUsername: String = "",
)
