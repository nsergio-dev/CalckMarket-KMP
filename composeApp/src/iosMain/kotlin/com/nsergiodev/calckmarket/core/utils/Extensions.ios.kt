package com.nsergiodev.calckmarket.core.utils

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual fun Any?.asCurrency(showDecimals: Boolean): String {
    val number = when (this) {
        is Number -> this.toDouble()
        is String -> this.toDoubleOrNull() ?: 0.0
        else -> 0.0
    }
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        locale = NSLocale(localeIdentifier = CurrencyConfig.localeIdentifier)
        val decimals = if (showDecimals) 2uL else 0uL
        minimumFractionDigits = decimals
        maximumFractionDigits = decimals
    }
    return formatter.stringFromNumber(NSNumber(number)) ?: "$this"
}