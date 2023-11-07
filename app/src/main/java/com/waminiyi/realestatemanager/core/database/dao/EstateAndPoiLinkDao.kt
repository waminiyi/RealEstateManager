package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.waminiyi.realestatemanager.core.database.model.EstateAndPoiLink


@Dao
interface  EstateAndPoiLinkDao {
    @Insert
    suspend fun insertEstateAndPoiLink(estateAndPoiLink: EstateAndPoiLink)

    @Delete
    suspend fun deleteEstateAndPoiLink(estateAndPoiLink: EstateAndPoiLink)
}