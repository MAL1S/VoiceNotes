package com.example.voicenotes.ui.notes

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.voicenotes.ui.services.PlayerWorker
import javax.inject.Inject

private const val PLAYER_TAG = "player_tag"

class NotesFragmentViewModel @Inject constructor(context: Context): ViewModel() {

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
}