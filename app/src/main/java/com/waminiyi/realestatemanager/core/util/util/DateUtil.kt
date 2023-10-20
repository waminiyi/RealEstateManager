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
    get()  {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(Date())
    }
