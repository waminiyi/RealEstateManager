package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest

/**
 * Class representing a point of interest.
 *
 * @property poiId The unique identifier (auto-generated) for the point of interest.
 * @property pointOfInterest The type of the point of interest.
 */
@Entity(tableName = "points_of_interest")
data class PointOfInterestEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "poi_id")
    val poiId: Int = 0,

    @ColumnInfo(name = "point_of_interest_type")
    val pointOfInterest: PointOfInterest
)
