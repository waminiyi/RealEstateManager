package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import kotlinx.coroutines.flow.Flow

interface EstateRepository {
    suspend fun saveEstate(estateWithDetails: EstateWithDetails): DataResult<Unit>
    fun getAllEstatesStream(): Flow<DataResult<List<Estate>>>
    suspend fun getEstateWithDetails(estateId: String): DataResult<EstateWithDetails?>
}