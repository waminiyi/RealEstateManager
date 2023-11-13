package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.model.data.WorkResult
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultEstateRepository @Inject constructor(
    private val estateDao: EstateDao,
    private val ioCoroutineDispatcher: CoroutineDispatcher
) : EstateRepository {
    override suspend fun addEstate(estateWithDetails: EstateWithDetails): WorkResult<String> {
        TODO("Not yet implemented")
    }

    override fun getAllEstatesStream(): Flow<WorkResult<List<Estate>>> {
        TODO("Not yet implemented")
    }

    override fun getEstateWithDetails(estateId: String): WorkResult<Estate?> {
        TODO("Not yet implemented")
    }
}