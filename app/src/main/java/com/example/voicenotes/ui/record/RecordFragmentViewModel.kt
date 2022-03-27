package com.example.voicenotes.ui.record

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.voicenotes.domain.usecase.FileUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RecordFragmentViewModel @Inject constructor(
    context: Context,
    private val fileNameUseCase: FileUseCase
): ViewModel() {

    fun getFileName() = fileNameUseCase.getFileName()
}