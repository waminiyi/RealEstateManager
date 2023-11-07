package com.waminiyi.realestatemanager.core.database.util

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}