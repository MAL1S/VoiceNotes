package com.example.voicenotes.domain.usecase

import com.example.voicenotes.data.repository.AuthVkRepositoryImpl
import com.vk.api.sdk.VK
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authVkRepositoryImpl: AuthVkRepositoryImpl
) {

    fun isLoggedIn(): Boolean {
        return VK.isLoggedIn()
    }

    fun auth(token: String) {
        authVkRepositoryImpl.auth(token = token)
    }

    fun logout() {
        authVkRepositoryImpl.logOut()
    }
}