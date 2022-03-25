package com.example.voicenotes.ui.player

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.provider.MediaStore
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PlayerService : Service() {

    private var player: MediaPlayer = MediaPlayer()

    override fun onCreate() {
        super.onCreate()

    }

    @SuppressLint("CheckResult")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Single.just(player)
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe({
                val path: String? = intent?.extras?.getString("path")
                player = MediaPlayer.create(this, Uri.parse(path))
                player.setVolume(100f, 100f)
                player.start()
            }, {

            })

        return START_STICKY
    }

    @SuppressLint("CheckResult")
    override fun onDestroy() {

        Single.just(player)
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe({
                player.stop()
                player.release()
            }, {

            })
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}