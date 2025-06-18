package com.example.chatterbox.chat.shared.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.chatterbox.chat.shared.domain.StorageRepository
import org.koin.core.context.GlobalContext
import com.example.chatterbox.BuildConfig
import com.example.chatterbox.core.common.SupabaseBuckets
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupabaseStorageRepository: StorageRepository {
    val TAG = "SupabaseStorageRepository"

    val retrofitSupabase = Retrofit.Builder()
        .baseUrl(BuildConfig.SUPABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val supabaseService = retrofitSupabase.create<SupabaseService>()

    override fun uploadImage(bucket: String, bytes: ByteArray, fileName: String) {
        Log.d(TAG, "Supabase url: ${BuildConfig.SUPABASE_URL}")
        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

        val call = supabaseService.uploadImage(
            bucket = bucket,
            path = "$fileName.jpg",
            authHeader = "Bearer ${BuildConfig.SUPABASE_KEY}",
            file = requestBody
        )
        
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(p0: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Upload successful: $fileName")
                    // Save this URL to Firestore or use directly
                } else {
                    Log.e(TAG, "Upload failed: ${response.code()} and ${response.raw()}")
                }
            }
            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                Log.e(TAG, "Upload error: ${p1.message}")
            }
        })

    }

    override fun deleteImage(bucket: String, fileName: String) {
        Log.d(TAG, "Supabase url: ${BuildConfig.SUPABASE_URL}")

        val call = supabaseService.deleteImage(
            bucket = bucket,
            path = "$fileName.jpg",
            authHeader = "Bearer ${BuildConfig.SUPABASE_KEY}"
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(p0: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Upload successful: $fileName")
                    // Save this URL to Firestore or use directly
                } else {
                    Log.e(TAG, "Upload failed: ${response.code()} and ${response.raw()}")
                }
            }
            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                Log.e(TAG, "Upload error: ${p1.message}")
            }
        })

    }


}