package com.example.voicenotes.domain.usecase

import com.example.voicenotes.data.repository.AuthVkRepositoryImpl
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authVkRepositoryImpl: AuthVkRepositoryImpl
) {

    fun ifLoggedIn(): Boolean {
        return authVkRepositoryImpl.ifLoggedIn()
    }

    fun auth(token: String) {
        authVkRepositoryImpl.auth(token = token)
    }

    fun logout() {
        authVkRepositoryImpl.logOut()
    }
}