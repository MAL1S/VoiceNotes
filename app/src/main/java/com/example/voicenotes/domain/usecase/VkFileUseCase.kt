package com.example.voicenotes.domain.usecase

import com.example.voicenotes.data.remote.dto.GetUploadServerResponse
import com.example.voicenotes.data.remote.dto.Response
import com.example.voicenotes.data.repository.VkFileRepositoryImpl
import io.reactivex.Single
import java.io.File
import javax.inject.Inject

class VkFileUseCase @Inject constructor(
    private val vkFileRepositoryImpl: VkFileRepositoryImpl
) {

    fun getUploadServer(): Single<Response> {
        return vkFileRepositoryImpl.getUploadServer()
    }

    fun uploadFile(file: File, uploadUrl: String) {
        vkFileRepositoryImpl.uploadFileToServer(file = file, uploadUrl = uploadUrl)
    }
}