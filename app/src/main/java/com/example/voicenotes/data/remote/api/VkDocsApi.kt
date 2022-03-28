package com.example.voicenotes.data.remote.api

import com.example.voicenotes.data.remote.dto.GetUploadDocsServerResponse
import com.example.voicenotes.data.remote.dto.GetUploadServerResponse
import com.example.voicenotes.data.remote.dto.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VkDocsApi {

    @GET("docs.getUploadServer")
    fun getUploadServer(
        @Query("access_token") token: String,
        @Query("v") v: Float = 5.131f
    ): Single<Response>

    @POST("docs.save")
    fun saveVoiceDoc(
        @Query("access_token") token: String,
        @Query("file") file: String,
        @Query("v") v: Float = 5.131f
    )
}