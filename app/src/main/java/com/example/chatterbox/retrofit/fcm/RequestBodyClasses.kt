package com.example.chatterbox.retrofit.fcm

data class V1MessageBody(
    val message: MessageContent
)

data class MessageContent(
    val token: String,
    val notification: NotificationData
)

data class NotificationData(
    val title: String,
    val body: String
)