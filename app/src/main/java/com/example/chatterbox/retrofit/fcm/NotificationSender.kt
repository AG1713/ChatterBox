package com.example.chatterbox.retrofit.fcm

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

object NotificationSender {

    fun sendFCMNotification(
        token: String,
        title: String,
        message: String,
        accessToken: String,
        fcmService: FCMService
    ) {
        val body = V1MessageBody(
            message = MessageContent(
                token = token,
                notification = NotificationData(
                    title = title,
                    body = message
                )
            )
        )

        val call = fcmService.sendNotification(body, "Bearer $accessToken")

        call.enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("FCM", "Success: ${response.code()}")
                    if (!response.isSuccessful) {
                        Log.e("FCM", "Error body: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("FCM", "Error: ${t.message}")
                }
            }
        )
    }


}