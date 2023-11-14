package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.ImageDao
import com.waminiyi.realestatemanager.core.database.model.asEstateEntity
import com.waminiyi.realestatemanager.core.database.model.asImageEntity
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.WorkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.*
import javax.inject.Inject

class DefaultEstateRepository @Inject constructor(
    private val estateDao: EstateDao,
    private val imageDao: ImageDao,
) : EstateRepository {
    override suspend fun saveEstate(estateWithDetails: EstateWithDetails): WorkResult<Unit> {
        return try {
            val images = estateWithDetails.images
            estateDao.upsertEstate(estateWithDetails.asEstateEntity())
            images.map {
                imageDao.upsertImage(it.asImageEntity(estateWithDetails.uuid))
            }
            WorkResult.Success(Unit)
        } catch (exception: IOException) {
            WorkResult.Error(exception)
        }
    }

    override fun getAllEstatesStream(): Flow<WorkResult<List<Estate>>> {
        return estateDao.getAllEstatesWithImages().map {
            WorkResult.Success(it.map { estateWithImage ->
                estateWithImage.asEstate()
            })
        }.catch<WorkResult<List<Estate>>> {
            emit(WorkResult.Error(it))
        }
    }

    override fun getEstateWithDetails(estateId: String): WorkResult<EstateWithDetails?> {
        return try {
            val result = estateDao.getEstateWithDetailsById(UUID.fromString(estateId))?.asEstateWithDetails()
            result?.let {
                WorkResult.Success(it)
            } ?: WorkResult.Error(NullPointerException("Estate with ID $estateId not found"))
        } catch (exception: IOException) {
            WorkResult.Error(exception)
        }
    }

}