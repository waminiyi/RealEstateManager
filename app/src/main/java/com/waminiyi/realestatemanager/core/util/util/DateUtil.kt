package com.waminiyi.realestatemanager.core.util.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * @return the current date in an appropriate format
 *
 */
val currentDate: String
    @SuppressLint("SimpleDateFormat")
    get() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(Date())
    }

fun getFormattedDate(date: Date?): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return date?.let { dateFormat.format(date) }.orEmpty()
}

