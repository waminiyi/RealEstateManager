package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.waminiyi.realestatemanager.core.database.model.PointOfInterestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PointOfInterestDao {
    @Insert
    suspend fun insertPointOfInterest(pointOfInterestEntity: PointOfInterestEntity):Long

    @Delete
    suspend fun deletePointOfInterest(pointOfInterestEntity: PointOfInterestEntity)

    @Query("SELECT * FROM points_of_interest WHERE poi_id = :poiId")
    fun getPointOfInterestById(poiId: Int): Flow<PointOfInterestEntity?>

    @Query("SELECT * FROM points_of_interest")
    fun getAllPointOfInterests(): Flow<List<PointOfInterestEntity>>
}
