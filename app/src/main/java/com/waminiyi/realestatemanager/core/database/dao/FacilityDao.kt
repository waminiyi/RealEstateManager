package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import com.waminiyi.realestatemanager.core.database.model.FacilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FacilityDao {
    @Insert
    suspend fun insertFacility(facilityEntity: FacilityEntity):Long

    @Delete
    suspend fun deleteFacility(facilityEntity: FacilityEntity)

    @Query("SELECT * FROM facilities WHERE facility_id = :facilityId")
    fun getFacilityById(facilityId: Int): FacilityEntity?

    @Query("SELECT * FROM facilities")
    fun getAllFacilities(): List<FacilityEntity>
}