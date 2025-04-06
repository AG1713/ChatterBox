package com.example.chatterbox.chat.domain

import com.google.firebase.Timestamp

data class User(
    val id: String,
    val username: String,
    val description: String,
    val profilePhotoUrl: String,
    val status: String,
    val lastActive: String,
    val dateCreated: Timestamp
)
