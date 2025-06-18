package com.example.chatterbox.chat.shared.data

import com.example.chatterbox.BuildConfig
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface SupabaseService {

    @PUT("storage/v1/object/{bucket}/{path}")
    fun uploadImage(
        @Path("bucket") bucket: String,
        @Path("path") path: String,
        @Header("Authorization") authHeader: String = "Bearer ${BuildConfig.SUPABASE_KEY}",
        @Header("Content-Type") contentType: String = "image/jpg",
        @Body file: RequestBody
    ): Call<ResponseBody>

    @DELETE("storage/v1/object/{bucket}/{path}")
    fun deleteImage(
        @Path("bucket") bucket: String,
        @Path("path") path: String,
        @Header("Authorization") authHeader: String = "Bearer ${BuildConfig.SUPABASE_KEY}",
        @Header("Content-Type") contentType: String = "image/jpg"
    ): Call<ResponseBody>

}