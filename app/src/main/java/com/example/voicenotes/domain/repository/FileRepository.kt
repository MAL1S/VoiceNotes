package com.example.voicenotes.domain.repository

import java.io.File

interface FileRepository {

    fun getFileName(): String

    fun saveFileToStorage(file: File)
}