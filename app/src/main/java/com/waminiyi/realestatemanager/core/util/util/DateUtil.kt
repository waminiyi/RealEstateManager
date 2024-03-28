package com.waminiyi.realestatemanager.core.util.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Retrieves the current date in the format "dd/MM/yyyy".
 *
 * @return The current date as a string in the format "dd/MM/yyyy".
 */
val currentDate: String
    get() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

/**
 * Formats the provided date object into the string format "dd/MM/yyyy".
 *
 * @param date The date object to be formatted.
 * @return The formatted date as a string in the format "dd/MM/yyyy". If the provided date is null, returns an empty string.
 */
fun getFormattedDate(date: Date?): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return date?.let { dateFormat.format(date) }.orEmpty()
}

