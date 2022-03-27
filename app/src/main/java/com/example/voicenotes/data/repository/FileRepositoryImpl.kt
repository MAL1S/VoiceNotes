package com.example.voicenotes.data.repository

import android.content.SharedPreferences
import com.example.voicenotes.domain.repository.FileRepository
import java.io.File
import javax.inject.Inject

private const val FILE_NAME = "file_name"

class FileNameSharedPrefImpl @Inject constructor(
    private val sharedPref: SharedPreferences
): FileRepository {

    override fun getFileName(): String {
        val currentIndex = sharedPref.getInt(FILE_NAME, 0)
        sharedPref.edit()
            .putInt(FILE_NAME, currentIndex+1)
            .apply()
        return "untitled $currentIndex"
    }

    override fun saveFileToStorage(file: File) {

    }
}