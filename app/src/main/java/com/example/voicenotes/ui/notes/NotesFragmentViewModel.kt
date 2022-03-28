package com.example.voicenotes.ui.notes

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.voicenotes.domain.usecase.AuthUseCase
import com.example.voicenotes.domain.usecase.VkFileUseCase
import com.example.voicenotes.ui.services.PlayerWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject


private const val PLAYER_TAG = "player_tag"

class NotesFragmentViewModel @Inject constructor(
    context: Context,
    private val authUseCase: AuthUseCase,
    private val vkFileUseCase: VkFileUseCase
): ViewModel() {

    private val workManager = WorkManager.getInstance(context)

    private lateinit var player: OneTimeWorkRequest

    init {
        workManager.cancelAllWork()
    }

    fun startPlayer(path: String) {
        val data = Data.Builder()
            .putString("path", path)
            .build()

        player = OneTimeWorkRequestBuilder<PlayerWorker>()
            .setInputData(data)
            .addTag(PLAYER_TAG)
            .build()
        workManager.enqueue(player)
    }

    fun stopPlayer() {
        Log.d("FFF", "STOOOOOOOOOOOOOOOOOP")
        workManager.cancelAllWorkByTag(PLAYER_TAG)
        workManager.cancelAllWork()
    }

    fun isLoggedIn(): Boolean {
        return authUseCase.isLoggedIn()
    }

    fun auth(token: String) {
        authUseCase.auth(token = token)
    }

    fun logout() {
        authUseCase.logout()
    }

    fun uploadFile(file: File) {
        Log.d("VVV", "$file")

        getUploadServer(file)
    }

    @SuppressLint("CheckResult")
    private fun getUploadServer(file: File) {
        Log.d("VVV", "called method")
        vkFileUseCase.getUploadServer()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("VVV", "$it")
                file
                vkFileUseCase.uploadFile(file = file, uploadUrl = it.response.uploadUrl)
                //Log.d("VVV", it.response.uploadUrl)
            }, {
                Log.d("VVV", "${it.message}")
            })
    }
}