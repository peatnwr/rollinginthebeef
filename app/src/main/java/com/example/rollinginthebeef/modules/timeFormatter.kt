package com.example.rollinginthebeef.modules

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun timeFormatter(dateOrder: String): String{
    try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.000Z'", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        val date = simpleDateFormat.parse(dateOrder)
        val dateFormat = SimpleDateFormat("kk:mm", Locale.getDefault())
        return dateFormat.format(date)
    } catch (e: Exception) {
        return ""
    }
}