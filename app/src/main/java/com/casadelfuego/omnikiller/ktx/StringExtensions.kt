package com.casadelfuego.omnikiller.ktx

import java.text.SimpleDateFormat
import java.util.*

fun String.toBoolean() = !isFalse()

fun String.isFalse() = isBlank() || this == "0" || "false".equals(this, true)

fun String.toDate() = safeParseDate(this, "yyyy-MM-dd")

fun String.toDateTime() = safeParseDate(this, "yyyy-MM-dd HH:mm:ss")

private fun safeParseDate(dateString: String, dateFormat: String): Date? {
    val dateFormatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    return try {
        dateFormatter.parse(dateString)
    } catch (ex: Exception) {
        null
    }
}
