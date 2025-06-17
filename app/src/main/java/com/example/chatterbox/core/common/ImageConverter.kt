package com.example.chatterbox.core.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

const val maxSizeBytes = 500 * 1024 // 500 KB, for example

fun convertToJpeg(context: Context, uri: Uri?): ByteArray? {
    if (uri == null) return null
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream) ?: return null

    var quality = 100
    var imageBytes: ByteArray

    do {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        imageBytes = outputStream.toByteArray()
        quality -= 5
    } while (imageBytes.size > maxSizeBytes && quality > 10)

    

    return imageBytes
}