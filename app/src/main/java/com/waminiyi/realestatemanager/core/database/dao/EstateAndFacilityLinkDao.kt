package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.waminiyi.realestatemanager.core.database.model.EstateAndFacilityLink
import com.waminiyi.realestatemanager.core.database.model.EstateAndPoiLink


@Dao
interface  EstateAndFacilityLinkDao {
    @Insert
    suspend fun insertEstateAndFacilityLink(estateAndFacilityLink: EstateAndFacilityLink)

    @Delete
    suspend fun deleteEstateAndFacilityLink(estateAndFacilityLink: EstateAndFacilityLink)

}