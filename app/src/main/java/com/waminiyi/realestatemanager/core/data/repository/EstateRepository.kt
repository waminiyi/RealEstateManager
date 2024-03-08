package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.database.model.FilterQueryParameter
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.Filter
import com.waminiyi.realestatemanager.core.util.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface EstateRepository : Syncable {
    suspend fun saveEstate(estateWithDetails: EstateWithDetails): DataResult<Unit>
    fun getAllEstatesStream(filter: Filter): Flow<DataResult<List<Estate>>>
    suspend fun getEstateWithDetails(estateId: String): DataResult<EstateWithDetails?>
    suspend  fun getEstatesToUpload():List<EstateEntity>
}