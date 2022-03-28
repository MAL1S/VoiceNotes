package com.example.voicenotes.data.remote.client

import com.example.voicenotes.data.remote.api.VkDocsApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.vk.com/method/"

object VkApiClient {

    private fun getVKApiInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Synchronized
    fun buildVkApi(): VkDocsApi {
        return getVKApiInstance().create(VkDocsApi::class.java)
    }
}