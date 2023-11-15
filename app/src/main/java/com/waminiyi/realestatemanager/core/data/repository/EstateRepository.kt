package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.Result
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import kotlinx.coroutines.flow.Flow

interface EstateRepository {
    suspend fun saveEstate(estateWithDetails: EstateWithDetails): Result<Unit>
    fun getAllEstatesStream(): Flow<Result<List<Estate>>>
    fun getEstateWithDetails(estateId: String): Result<EstateWithDetails?>
}