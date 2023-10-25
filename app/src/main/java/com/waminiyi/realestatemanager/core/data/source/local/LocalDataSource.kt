package com.waminiyi.realestatemanager.core.data.source.local

import com.waminiyi.realestatemanager.core.domain.model.WorkResult
import com.waminiyi.realestatemanager.core.domain.model.estate.Estate
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun addEstate(estate: Estate) : WorkResult<String>
    fun getAllEstatesStream(): Flow<WorkResult<List<Estate>>>
    fun getEstateStream(estateId: String): Flow<WorkResult<Estate?>>
}