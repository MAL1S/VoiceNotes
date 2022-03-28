package com.example.voicenotes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("response")
    val response: GetUploadServerResponse
)
