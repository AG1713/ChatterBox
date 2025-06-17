package com.example.chatterbox.retrofit.token

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TokenClient {
    private const val BASE_URL = "https://fcm-token-backend.onrender.com/"

    val retrofit: TokenService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenService::class.java)
    }


}