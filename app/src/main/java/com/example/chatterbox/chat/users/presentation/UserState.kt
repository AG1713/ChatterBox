package com.example.chatterbox.chat.users.presentation

import androidx.compose.runtime.Immutable
import com.example.chatterbox.chat.users.domain.User

@Immutable
data class UserState(
    val isLoading: Boolean = true,
    val user: User? = null,
)
