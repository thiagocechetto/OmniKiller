package com.casadelfuego.omnikiller.ktx

import java.text.SimpleDateFormat
import java.util.*

fun String.toBoolean() = !isFalse()

fun String.isFalse() = isBlank() || this == "0" || "false".equals(this, true)

fun String.toDate(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.parse(this)
}

fun String.toDateTime(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.parse(this)
}