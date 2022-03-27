package com.example.voicenotes.utils

fun convertTimeToString(time: Int): String {
    return "${time / 60}:${if (time % 60 < 10) 0 else ""}${time % 60}"
}