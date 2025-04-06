package com.example.chatterbox.chat.domain

import com.google.firebase.Timestamp

data class ChatRoom(
    val id: String,
    val type: String,
    val groupName: String,
    val groupDescription: String,
    val groupPhotoUrl: String,
    val lastMessageSentTime: Timestamp,
    val lastMessageSentUserId: String,
    val lastMessageSentUsername: String,
    val lastMessageSent: String
)
