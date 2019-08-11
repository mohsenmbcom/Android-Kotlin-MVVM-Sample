package com.mohsenmb.googlenewsapisample.repository

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toLocalDate(): Date {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    // According to the NewsApi docs, timezone is UTC (+000)
    format.timeZone = TimeZone.getTimeZone("Etc/UTC")
    return try {
        format.parse(this)
    } catch (e: ParseException) {
        Date(0L) // will not show the date if the date is zero!
    }
}