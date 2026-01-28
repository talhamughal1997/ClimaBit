package com.talhapps.climabit.core.ui.components.util

fun formatTime(hour: Int, minute: Int): String {
    val period = if (hour < 12) "AM" else "PM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val displayMinute = if (minute == 0) "" else ":${minute.toString().padStart(2, '0')}"
    return "$displayHour$displayMinute $period"
}

