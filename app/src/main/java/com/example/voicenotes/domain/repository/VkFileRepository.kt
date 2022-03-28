package com.example.voicenotes.domain.repository

import com.example.voicenotes.data.remote.dto.GetUploadDocsServerResponse
import com.example.voicenotes.data.remote.dto.GetUploadServerResponse
import com.example.voicenotes.data.remote.dto.Response
import io.reactivex.Single
import java.io.File

interface VkFileRepository {

    fun getUploadServer(): Single<Response>

    fun uploadFileToServer(file: File, uploadUrl: String)
}