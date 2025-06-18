package com.example.chatterbox.chat.shared.domain

interface StorageRepository {
    fun uploadImage(bucket: String, bytes: ByteArray, fileName: String)
    fun deleteImage(bucket: String, fileName: String)
}