package com.example.chatterbox.chat.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Member (
    val id: String = "",
    val username: String = "",
)