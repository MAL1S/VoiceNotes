package com.example.voicenotes.ui.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.example.voicenotes.ui.notes.NotesFragment

class PlayerWorker(appContext: Context, workerParams: WorkerParameters) :
    ListenableWorker(appContext, workerParams) {

    //private var player: MediaPlayer? = null

    override fun startWork(): ListenableFuture<Result> {
//        val path = inputData.getString("path")
//
//        player = MediaPlayer.create(applicationContext, Uri.parse(path))
//
//        player!!.setVolume(100f, 100f)
//        player!!.start()

        return CallbackToFutureAdapter.getFuture {
            if (!NotesFragment.isFirstPlaying) {
                val path = inputData.getString("path")

                NotesFragment.apply {
                    player = MediaPlayer.create(applicationContext, Uri.parse(path))
                    Log.d("FFF", "================START ${player}")
                    player!!.setVolume(100f, 100f)
                    player!!.seekTo(currentPosition)
                    player!!.start()
                    isPlaying = true
                    playerLiveData.value = 1
                }
            }
        }
    }

    override fun onStopped() {
        super.onStopped()
        //Toast.makeText(applicationContext, "stopped", Toast.LENGTH_SHORT).show()
        Log.d("FFF", "====================STOP")

        NotesFragment.apply {
            if (player != null) {
                //currentPosition = player!!.currentPosition
                isPlaying = false
                player!!.stop()
                player!!.release()
            }
        }
    }
}
//
//class PlayerService : Service() {
//
//    private var player: MediaPlayer = MediaPlayer()
//
//    override fun onCreate() {
//        super.onCreate()
//
//    }
//
//    @SuppressLint("CheckResult")
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Single.just(player)
//            .subscribeOn(Schedulers.newThread())
//            .observeOn(Schedulers.newThread())
//            .subscribe({
//                val path: String? = intent?.extras?.getString("path")
//                player = MediaPlayer.create(this, Uri.parse(path))
//                NotesFragment.player = player
//                player.setVolume(100f, 100f)
//                player.start()
//            }, {
//
//            })
//
//        return START_STICKY
//    }
//
//    @SuppressLint("CheckResult")
//    override fun onDestroy() {
//
//        Single.just(player)
//            .subscribeOn(Schedulers.newThread())
//            .observeOn(Schedulers.newThread())
//            .subscribe({
//                player.stop()
//                player.release()
//            }, {
//
//            })
//        super.onDestroy()
//    }
//
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//}