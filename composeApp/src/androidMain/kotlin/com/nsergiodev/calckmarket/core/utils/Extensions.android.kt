package com.nsergiodev.calckmarket.core.utils

import java.text.NumberFormat
import java.util.Locale

actual fun Any?.asCurrency(): String {
    val number = when (this) {
        is Number -> this.toDouble()
        is String -> this.toDoubleOrNull() ?: 0.0
        else -> 0.0
    }
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }
    return formatter.format(number)
}