package com.waminiyi.realestatemanager.data.database.util

import androidx.room.TypeConverter
import com.waminiyi.realestatemanager.data.models.PointOfInterest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun poiListToString(value: List<PointOfInterest>): String = Json.encodeToString(value)

    @TypeConverter
    fun poiListFromString(value: String): List<PointOfInterest> = Json.decodeFromString(value)
}