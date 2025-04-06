package com.example.chatterbox

import android.app.Application
import com.example.chatterbox.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ChatterBoxApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // For starting Koin
        startKoin {
            androidContext(this@ChatterBoxApp)
            androidLogger()

            modules(appModule)
        }
    }

}