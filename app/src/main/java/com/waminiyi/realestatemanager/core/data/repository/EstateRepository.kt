package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.WorkResult
import com.waminiyi.realestatemanager.core.model.data.Estate
import kotlinx.coroutines.flow.Flow

interface EstateRepository {
    suspend fun addEstate(estate: Estate): WorkResult<String>
    fun getAllEstatesStream(): Flow<WorkResult<List<Estate>>>
    fun getEstateStream(estateId: String): Flow<WorkResult<Estate?>>
}