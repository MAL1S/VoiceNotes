package com.example.voicenotes.domain.repository

interface AuthVKRepository {

    fun auth(token: String)

    fun logOut()
}