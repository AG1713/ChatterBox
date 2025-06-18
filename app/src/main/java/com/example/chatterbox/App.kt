package com.example.chatterbox

import android.app.Application
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.example.chatterbox.di.appModule
import com.example.chatterbox.retrofit.fcm.FCMService
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {
    val currentUserid
    get() = FirebaseAuth.getInstance().currentUser?.uid

    public val coilReloadState = mutableLongStateOf(System.currentTimeMillis())

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(appModule)
        }

        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let {
                HeartbeatManager.startHeartbeat(it.uid)
            } ?: HeartbeatManager.stopHeartbeat()
        }

    }
}