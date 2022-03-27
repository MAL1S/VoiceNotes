package com.example.voicenotes.domain.usecase

import com.example.voicenotes.data.repository.FileNameSharedPrefImpl
import java.io.File
import javax.inject.Inject

class FileUseCase @Inject constructor(
    private val fileRepositoryImpl: FileNameSharedPrefImpl
) {

    fun getFileName(): String {
        return fileRepositoryImpl.getFileName()
    }
}