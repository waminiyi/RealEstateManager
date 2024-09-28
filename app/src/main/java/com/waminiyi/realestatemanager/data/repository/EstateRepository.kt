package com.waminiyi.realestatemanager.data.repository

import com.waminiyi.realestatemanager.data.models.Result
import com.waminiyi.realestatemanager.data.models.Estate
import com.waminiyi.realestatemanager.data.models.EstateWithDetails
import kotlinx.coroutines.flow.Flow

interface EstateRepository {
    suspend fun saveEstate(estateWithDetails: EstateWithDetails): Result<Unit>
    fun getAllEstatesStream(): Flow<Result<List<Estate>>>
    suspend fun getEstateWithDetails(estateId: String): Result<EstateWithDetails?>
}