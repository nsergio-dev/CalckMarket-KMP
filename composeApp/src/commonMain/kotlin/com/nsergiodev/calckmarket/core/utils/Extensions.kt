package com.nsergiodev.calckmarket.core.utils

expect fun Any?.asCurrency(): String

fun String?.isEmptySafe(): Boolean {
    return  !this.isNullOrEmpty()
            && this.isNotEmpty()
            && this.isNotBlank()
}