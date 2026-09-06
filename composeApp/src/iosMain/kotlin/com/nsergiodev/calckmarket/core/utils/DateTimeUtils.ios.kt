package com.nsergiodev.calckmarket.core.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970

actual fun getCurrentEpochMillis(): Long {
    val seconds = NSDate().timeIntervalSince1970
    return (seconds * 1000).toLong()
}

actual fun formatEpochToReadableDate(epochMillis: Long): String {
    val date = NSDate.dateWithTimeIntervalSince1970(epochMillis / 1000.0)
    val formatter = NSDateFormatter().apply {
        dateFormat = "d MMM yyyy, h:mm a"
        locale = NSLocale(localeIdentifier = "es_CO")
    }
    return formatter.stringFromDate(date)
}
