package com.nsergiodev.calckmarket.core.utils

expect fun Any?.asCurrency(showDecimals: Boolean = CurrencyConfig.showDecimals): String

fun String?.isNotNullOrBlankSafe(): Boolean {
    return !this.isNullOrBlank()
}

@Deprecated("Usa isNotNullOrBlankSafe para mayor claridad semántica", ReplaceWith("isNotNullOrBlankSafe()"))
fun String?.isEmptySafe(): Boolean {
    return isNotNullOrBlankSafe()
}