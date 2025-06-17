package com.example.chatterbox.retrofit.token

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

interface TokenService {

    @GET("token")
    fun getAccessToken(): Call<TokenResponse>

}

data class TokenResponse(
    @SerializedName("accessToken") val accessToken: String
)