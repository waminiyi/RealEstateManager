package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import java.util.*

/**
 * Class representing the link between an estate and a facility.
 *
 * @property estateUuid The unique identifier (UUID) of the estate.
 * @property facilityId The identifier of the facility.
 */
@Entity(
    tableName = "estate_and_facility_link",
    primaryKeys = ["estate_uuid", "facility_id"],
    indices = [Index("facility_id")]
)
data class EstateAndFacilityLink(
    @ColumnInfo(name = "estate_uuid")
    val estateUuid: UUID,

    @ColumnInfo(name = "facility_id")
    val facilityId: Int
)