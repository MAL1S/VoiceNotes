package com.example.voicenotes.ui.record

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

private val rndNumber = (0..100000).random()
private val FILE_NAME = "basic_file_name_if_user_input_was_empty:$rndNumber"

class Recorder(
    private val context: Context
) {

    var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    lateinit var recorderDisposable: Disposable

    fun getState(): Boolean = state

    fun startRecording(fileName: String) {
        recorderDisposable = Single.just(fileName)
            .observeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                try {
                    mediaRecorder =
                        MediaRecorder() //без конструктора с контекстом, потому что он только на 31+ апи

                    mediaRecorder?.apply {
                        setAudioSource(MediaRecorder.AudioSource.MIC)
                        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                        setAudioEncodingBitRate(16 * 44100)
                        setAudioSamplingRate(44100)

                        output = if (fileName != "") {
                            Log.d("AAA", fileName)
                            context.filesDir.absolutePath + "/$fileName.m4a"
                        } else {
                            Log.d("AAA", FILE_NAME)
                            context.filesDir.absolutePath + "/$FILE_NAME.m4a"
                        }
                        setOutputFile(output)

                        prepare()
                        start()
                    }
                    state = true
                    //Toast.makeText(context, "Recording started!", Toast.LENGTH_SHORT).show()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }, {

            })
    }

    fun stopRecording() {
        if (state) {
            mediaRecorder?.stop()
            //mediaRecorder?.release()
            state = false
        } else {
            //Toast.makeText(context, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveFile(fileName: String?) {
        if (fileName != null) {
            val oldFile = File(context.filesDir.absolutePath + "/$FILE_NAME.m4a")
            val newFile = File(context.filesDir.absolutePath + "/$fileName.m4a")

            try {
                val outputStream = FileOutputStream(File(newFile.absolutePath.toString()), true)
                outputStream.write(oldFile.readBytes())
                oldFile.delete()
            } catch (e: Exception) {
                Log.e("AAA", "${e.message}")
            }
        }
    }

    fun deleteFile() {
        val oldFile = File(context.filesDir.absolutePath + "/$FILE_NAME.m4a")
        oldFile.delete()
    }

    fun releasePlayer() {
        mediaRecorder?.release()
        recorderDisposable.dispose()
    }
}