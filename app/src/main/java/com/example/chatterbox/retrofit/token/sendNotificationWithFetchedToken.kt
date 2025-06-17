package com.example.chatterbox.retrofit.token

import android.util.Log
import com.example.chatterbox.retrofit.fcm.FCMService
import com.example.chatterbox.retrofit.fcm.NotificationSender
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun sendNotificationWithFetchedToken(
    userFcmToken: String,
    title: String,
    message: String,
    fcmService: FCMService
) {
    TokenClient.retrofit.getAccessToken().enqueue(object : Callback<TokenResponse> {
        override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
            if (response.isSuccessful) {
                val accessToken = response.body()?.accessToken ?: return
                NotificationSender.sendFCMNotification(
                    token = userFcmToken,
                    title = title,
                    message = message,
                    accessToken = accessToken,
                    fcmService = fcmService
                )
            } else {
                Log.e("Token", "Failed to fetch token: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
            Log.e("Token", "Token fetch error: ${t.message}")
        }
    })
}
