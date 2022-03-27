package com.example.voicenotes

import android.app.Application
import com.example.voicenotes.di.AppComponent
import com.example.voicenotes.di.DaggerAppComponent

class App: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(applicationContext)
    }
}