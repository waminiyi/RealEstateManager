package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.WorkResult
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import kotlinx.coroutines.flow.Flow

interface EstateRepository {
    suspend fun addEstate(estateWithDetails: EstateWithDetails): WorkResult<String>
    fun getAllEstatesStream(): Flow<WorkResult<List<Estate>>>
    fun getEstateWithDetails(estateId: String): WorkResult<Estate?>
}