package com.example.voicenotes.data.repository

import com.example.voicenotes.domain.repository.AuthVKRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult

class AuthVkRepositoryImpl: AuthVKRepository {
    override fun auth() {
//        val authLauncher = VK.login(activity) { result : VKAuthenticationResult ->
//            when (result) {
//                is VKAuthenticationResult.Success -> {
//                    // User passed authorization
//                }
//                is VKAuthenticationResult.Failed -> {
//                    // User didn't pass authorization
//                }
//            }
//        }
    }

    override fun logOut() {
        TODO("Not yet implemented")
    }
}