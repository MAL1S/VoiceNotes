package com.example.voicenotes.di

import android.content.Context
import android.content.SharedPreferences
import com.example.voicenotes.data.remote.api.VkDocsApi
import com.example.voicenotes.data.remote.client.VkApiClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideVkDocsApi(): VkDocsApi {
        return VkApiClient.buildVkApi()
    }
}