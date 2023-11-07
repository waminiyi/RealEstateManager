package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import java.util.*

/**
 * Class representing the link between an estate and a point of interest (POI).
 *
 * @property estateUuid The unique identifier (UUID) of the estate.
 * @property poiId The identifier of the point of interest.
 */
@Entity(tableName = "estate_and_poi_link",
    primaryKeys = ["estate_uuid", "poi_id"],
    indices = [Index("poi_id")])
data class EstateAndPoiLink(
    @ColumnInfo(name = "estate_uuid")
    val estateUuid: UUID,

    @ColumnInfo(name = "poi_id")
    val poiId: Int
)

