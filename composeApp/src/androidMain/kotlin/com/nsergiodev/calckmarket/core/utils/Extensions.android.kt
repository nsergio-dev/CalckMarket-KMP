package com.nsergiodev.calckmarket.core.utils

import java.text.NumberFormat
import java.util.Locale

actual fun Any?.asCurrency(showDecimals: Boolean): String {
    val number = when (this) {
        is Number -> this.toDouble()
        is String -> this.toDoubleOrNull() ?: 0.0
        else -> 0.0
    }
    val locale = Locale.Builder().setLanguage("es").setRegion("CO").build()
    val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        val decimals = if (showDecimals) 2 else 0
        minimumFractionDigits = decimals
        maximumFractionDigits = decimals
    }
    return formatter.format(number)
}