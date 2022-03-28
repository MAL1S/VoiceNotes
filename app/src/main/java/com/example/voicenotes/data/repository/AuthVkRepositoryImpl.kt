package com.example.voicenotes.data.repository

import android.content.SharedPreferences
import com.example.voicenotes.domain.repository.AuthVKRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import javax.inject.Inject

private const val AUTH_SHARED_PREF = "auth_shared_pref"

class AuthVkRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : AuthVKRepository {

    override fun ifLoggedIn(): Boolean {
        return sharedPreferences.getString(AUTH_SHARED_PREF, null) != null
    }

    override fun auth(token: String) {
        sharedPreferences.edit()
            .putString(AUTH_SHARED_PREF, token)
            .apply()
    }

    override fun logOut() {
        sharedPreferences.edit()
            .putString(AUTH_SHARED_PREF, null)
            .apply()
    }
}