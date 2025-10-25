package com.nsergiodev.calckmarket

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform