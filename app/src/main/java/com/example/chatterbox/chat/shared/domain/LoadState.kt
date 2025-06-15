package com.example.chatterbox.chat.shared.domain

sealed class LoadState() {
    object Idle: LoadState()
    object Loading: LoadState()
    data class Error(val exception: Throwable?): LoadState()
}