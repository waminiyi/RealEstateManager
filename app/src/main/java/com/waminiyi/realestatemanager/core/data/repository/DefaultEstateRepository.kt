package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.WorkResult
import com.waminiyi.realestatemanager.core.model.data.Estate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultEstateRepository @Inject constructor(
    private val ioCoroutineDispatcher: CoroutineDispatcher
) : EstateRepository {
    override suspend fun addEstate(estate: Estate): WorkResult<String> {
        TODO("Not yet implemented")
    }

    override fun getAllEstatesStream(): Flow<WorkResult<List<Estate>>> {
        TODO("Not yet implemented")
    }

    override fun getEstateStream(estateId: String): Flow<WorkResult<Estate?>> {
        TODO("Not yet implemented")
    }
}