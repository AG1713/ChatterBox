package com.example.chatterbox.chat.groups.domain

import com.example.chatterbox.chat.shared.domain.Member
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: String = "",
    val name: String = "",
    val groupPhotoUrl: String = "",
    val description: String = "",
    val creationTimestamp: Long = System.currentTimeMillis(),
    val memberIds: List<String> = listOf(),
    val members: List<Member> = listOf(),
    val lastMessage: String = "",
    val lastMessageUserId: String = "",
    val lastMessageUsername: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
)
