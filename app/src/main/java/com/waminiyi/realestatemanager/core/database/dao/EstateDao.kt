package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.*
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.database.model.EstateWithDetailsEntity
import com.waminiyi.realestatemanager.core.database.model.EstateWithImage
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EstateDao {
    @Upsert
    suspend fun upsertEstate(estateEntity: EstateEntity)

    @Transaction
    @Query("SELECT * FROM estates")
    fun getAllEstatesWithImages(): Flow<List<EstateWithImage>>

    @Transaction
    @Query("SELECT * FROM estates WHERE estate_uuid = :estateUuid")
    fun getEstateWithDetailsById(estateUuid: UUID): EstateWithDetailsEntity?

}
