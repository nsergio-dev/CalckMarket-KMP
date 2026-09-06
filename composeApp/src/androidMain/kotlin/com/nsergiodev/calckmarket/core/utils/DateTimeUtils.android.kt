package com.nsergiodev.calckmarket.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun getCurrentEpochMillis(): Long = System.currentTimeMillis()

actual fun formatEpochToReadableDate(epochMillis: Long): String {
    val date = Date(epochMillis)
    val locale = Locale.Builder().setLanguage("es").setRegion("CO").build()
    val formatter = SimpleDateFormat("d MMM yyyy, h:mm a", locale)
    return formatter.format(date)
}
