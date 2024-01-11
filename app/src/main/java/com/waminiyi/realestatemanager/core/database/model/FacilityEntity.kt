package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.model.data.FacilityType

/**
 * Class representing a facility .
 *
 * @property facilityId The  identifier (auto-generated) for the facility.
 * @property type The type of the facility (e.g., BEDROOM, BATHROOM, etc.).
 * @property count The count of the specific facility type.
 */
@Entity(tableName = "facilities")
data class FacilityEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "facility_id")
    val facilityId: Int = 0,

    @ColumnInfo(name = "facility_type")
    val type: FacilityType,

    @ColumnInfo(name = "facility_count")
    val count: Int
)
