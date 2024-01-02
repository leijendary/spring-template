package com.leijendary.extension

fun Long.elapsedTime(): String {
    val hours = this / (1000 * 60 * 60) % 24
    val minutes = this / (1000 * 60) % 60
    val seconds = (this / 1000) % 60

    return "${hours}h ${minutes}m ${seconds}s"
}
