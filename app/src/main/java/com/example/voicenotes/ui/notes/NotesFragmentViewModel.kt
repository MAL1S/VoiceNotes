package com.example.voicenotes.ui.notes

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.voicenotes.domain.usecase.AuthUseCase
import com.example.voicenotes.ui.services.PlayerWorker
import com.google.common.util.concurrent.ListenableFuture
import com.vk.api.sdk.VK
import java.util.concurrent.ExecutionException
import javax.inject.Inject


private const val PLAYER_TAG = "player_tag"

class NotesFragmentViewModel @Inject constructor(
    context: Context,
    private val authUseCase: AuthUseCase
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
        return authUseCase.ifLoggedIn()
    }

    fun auth(token: String) {
        authUseCase.auth(token = token)
    }

    fun logout() {
        authUseCase.logout()
    }

//    fun uploadFiles() {
//        VK.executeSync()
//    }
}