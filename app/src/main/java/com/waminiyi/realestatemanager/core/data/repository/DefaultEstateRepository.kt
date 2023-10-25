package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.data.source.local.LocalDataSource
import com.waminiyi.realestatemanager.core.domain.model.WorkResult
import com.waminiyi.realestatemanager.core.domain.model.estate.Estate
import com.waminiyi.realestatemanager.core.domain.repository.EstateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultEstateRepository @Inject constructor(private val localDataSource: LocalDataSource) : EstateRepository {
    override suspend fun addEstate(estate: Estate): WorkResult<String> {
        return localDataSource.addEstate(estate)
    }

    override fun getAllEstatesStream(): Flow<WorkResult<List<Estate>>> {
        return localDataSource.getAllEstatesStream()
    }

    override fun getEstateStream(estateId: String): Flow<WorkResult<Estate?>> {
        return localDataSource.getEstateStream(estateId)
    }

}