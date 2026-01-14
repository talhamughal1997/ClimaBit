package com.talhapps.climabit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform