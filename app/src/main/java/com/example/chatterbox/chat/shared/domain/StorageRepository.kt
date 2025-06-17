package com.example.chatterbox.chat.shared.domain

interface StorageRepository {
    fun uploadImage(bytes: ByteArray, fileName: String)
}