package com.example.voicenotes.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.voicenotes.data.remote.api.VkDocsApi
import com.example.voicenotes.data.remote.dto.GetUploadDocsServerResponse
import com.example.voicenotes.data.remote.dto.GetUploadServerResponse
import com.example.voicenotes.data.remote.dto.Response
import com.example.voicenotes.domain.repository.VkFileRepository
import com.vk.api.sdk.*
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import javax.inject.Inject


class VkFileRepositoryImpl @Inject constructor(
    private val vkDocsApi: VkDocsApi,
    private val vkRepositoryImpl: AuthVkRepositoryImpl
) : VkFileRepository {

    override fun getUploadServer(): Single<Response> {
        Log.d("VVV", "${vkRepositoryImpl.getToken()}")
        val a = vkDocsApi.getUploadServer(vkRepositoryImpl.getToken())
        Log.d("VVV", "${a}")
        return a
    }

    @SuppressLint("CheckResult")
    override fun uploadFileToServer(file: File, uploadUrl: String) {

        Single.just(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("VVV", "uploading started")
                VK.execute(FileUploadCommand(uploadUrl, Uri.fromFile(file)), object:
                    VKApiCallback<FileUploadCommand.UploadResult> {
                    override fun success(result: FileUploadCommand.UploadResult) {
                        Log.d("VVV", "token = ${vkRepositoryImpl.getToken()} file = ${result.file}")
                        vkDocsApi.saveVoiceDoc(token = vkRepositoryImpl.getToken(), file = result.file)
                    }

                    override fun fail(error: Exception) {
                        Log.d("VVV", "error = ${error.message}")
                    }
                })
            }, {
                Log.d("VVV", "ERRROR = ${it.message}")
            })
    }
}

class FileUploadCommand(
    private val uploadUrl: String,
    private val audio: Uri
) : ApiCommand<FileUploadCommand.UploadResult>() {

    override fun onExecute(manager: VKApiManager): UploadResult {
        val fileUploadCall = VKHttpPostCall.Builder()
            .url(uploadUrl)
            .args("file", audio)
            .build()
        Log.d("VVV", "${fileUploadCall.isMultipart} ${fileUploadCall.parts}")
        try {
            val a = manager.execute(fileUploadCall, null, UploadResultParser())
            return a
        } catch (e: java.lang.Exception) {
            Log.d("VVV", "${e.message}")
            return UploadResult("123")
        }
    }

    data class UploadResult(
        val file: String,
//        val redirect: String,
//        val audio: String,
//        val hash: String
    )

    private class UploadResultParser: VKApiJSONResponseParser<UploadResult> {

        override fun parse(responseJson: JSONObject): UploadResult =
            try {
                Log.d("VVV", "$responseJson")
                val a = UploadResult(
                    file = responseJson.getString("file"),
//                    redirect = responseJson.getString("redirect"),
//                    hash = responseJson.getString("hash"),
//                    audio = responseJson.getString("audio")
                )
                Log.d("VVV", "$a")
                a
            } catch (ex: JSONException) {
                Log.d("VVV", "${ex.message}")
                throw VKApiIllegalResponseException(ex)
            }

    }
}