package com.example.voicenotes

import android.app.Application
import com.example.voicenotes.di.AppComponent
import com.example.voicenotes.di.DaggerAppComponent
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.vk.api.sdk.VKDefaultValidationHandler
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.utils.log.DefaultApiLogger
import com.vk.api.sdk.utils.log.Logger

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        VK.setConfig(
            VKApiConfig(
                version = "5.131",
                context = this,
                appId = 8117881,
                logger = DefaultApiLogger(lazy { Logger.LogLevel.VERBOSE }, "VKSdkApi"),
                validationHandler = VKDefaultValidationHandler(this),
            )
        )
    }

//    override fun onCreate() {
//        super.onCreate()
//        VK.addTokenExpiredHandler(tokenTracker)
//    }
//
//    private val tokenTracker = object: VKTokenExpiredHandler {
//        override fun onTokenExpired() {
//            // token expired
//        }
//    }
}