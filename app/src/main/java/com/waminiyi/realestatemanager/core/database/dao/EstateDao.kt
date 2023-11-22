package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.*
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.database.model.EstateWithDetailsEntity
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EstateDao {
    @Upsert
    suspend fun upsertEstate(estateEntity: EstateEntity)

    @Transaction
    @Query("SELECT * FROM estates WHERE estate_uuid = :estateUuid")
    fun getEstateWithDetailsById(estateUuid: UUID): EstateWithDetailsEntity?

    @Transaction
    @Query(
        "SELECT * FROM estates" +
                " LEFT JOIN images ON estates.estate_uuid = images.estate_uuid" +
                " WHERE is_main_image = 1"
    )
    fun getAllEstatesWithImages(): Flow<Map<EstateEntity, List<PhotoEntity>>>

}
