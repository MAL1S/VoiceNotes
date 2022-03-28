package com.example.voicenotes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GetUploadServerResponse(
    @SerializedName("upload_url")
    val uploadUrl: String
)
