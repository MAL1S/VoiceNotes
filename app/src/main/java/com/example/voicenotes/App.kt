package com.example.voicenotes

import android.app.Application
import com.example.voicenotes.di.AppComponent
import com.example.voicenotes.di.DaggerAppComponent
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class App: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            // token expired
        }
    }
}