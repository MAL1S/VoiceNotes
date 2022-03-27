package com.example.voicenotes.ui.notes

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.voicenotes.ui.services.PlayerWorker
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException
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

    fun isWorkScheduled(): Boolean {
        val instance = WorkManager.getInstance()
        val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(PLAYER_TAG)
        return try {
            var running = false
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            running
        } catch (e: ExecutionException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }
}