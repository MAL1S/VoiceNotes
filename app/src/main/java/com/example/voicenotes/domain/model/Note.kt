package com.example.voicenotes.domain.model

data class Note(
    val title: String,
    val path: String,
    var currentDuration: Int?,
    val overallDuration: Int?,
    var isPlaying: Boolean
)
