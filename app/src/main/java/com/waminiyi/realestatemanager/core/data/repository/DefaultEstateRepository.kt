package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.ImageDao
import com.waminiyi.realestatemanager.core.database.model.asEstate
import com.waminiyi.realestatemanager.core.database.model.asEstateEntity
import com.waminiyi.realestatemanager.core.database.model.asPhotoEntity
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.Result
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
    override suspend fun saveEstate(estateWithDetails: EstateWithDetails): Result<Unit> {
        return try {
            val images = estateWithDetails.photos
            estateDao.upsertEstate(estateWithDetails.asEstateEntity())
            images.map {
                imageDao.upsertImage(it.asPhotoEntity(estateWithDetails.uuid))
            }
            Result.Success(Unit)
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    override fun getAllEstatesStream(): Flow<Result<List<Estate>>> {
        return estateDao.getAllEstatesWithImages().map {
            Result.Success(it.map { estateWithImage ->
                estateWithImage.asEstate()
            })
        }.catch<Result<List<Estate>>> {
            emit(Result.Error(it))
        }
    }

    override fun getEstateWithDetails(estateId: String): Result<EstateWithDetails?> {
        return try {
            val result = estateDao.getEstateWithDetailsById(UUID.fromString(estateId))?.asEstateWithDetails()
            result?.let {
                Result.Success(it)
            } ?: Result.Error(NullPointerException("Estate with ID $estateId not found"))
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

}