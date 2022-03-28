package com.example.voicenotes.domain.repository

interface AuthVKRepository {

    fun ifLoggedIn(): Boolean

    fun auth(token: String)

    fun logOut()
}