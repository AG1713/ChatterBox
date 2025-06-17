package com.example.chatterbox.chat.users.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val description: String = "",
    val profilePhotoUrl: String = "",
    var lastActive: Long = System.currentTimeMillis(),
    val dateCreated: Long = System.currentTimeMillis(),
    val messageToken: String = ""
)
