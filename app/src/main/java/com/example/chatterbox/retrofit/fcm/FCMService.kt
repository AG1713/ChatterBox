package com.example.chatterbox.retrofit.fcm

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FCMService {
    @POST("v1/projects/chatterbox-28db5/messages:send")
    fun sendNotification(
        @Body body: V1MessageBody,
        @Header("Authorization") authHeader: String
    ): Call<ResponseBody>
}