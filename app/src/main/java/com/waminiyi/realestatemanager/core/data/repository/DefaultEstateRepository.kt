package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.database.model.asEstate
import com.waminiyi.realestatemanager.core.database.model.asEstateEntity
import com.waminiyi.realestatemanager.core.database.model.asPhotoEntity
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.*
import javax.inject.Inject

class DefaultEstateRepository @Inject constructor(
    private val estateDao: EstateDao,
    private val photoDao: PhotoDao,
) : EstateRepository {
    override suspend fun saveEstate(estateWithDetails: EstateWithDetails): DataResult<Unit> {
        return try {
            val images = estateWithDetails.photos
            estateDao.upsertEstate(estateWithDetails.asEstateEntity())
            images.map {
                photoDao.upsertImage(it.asPhotoEntity(estateWithDetails.uuid))
            }
            DataResult.Success(Unit)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override fun getAllEstatesStream(): Flow<DataResult<List<Estate>>> {
        return estateDao.getAllEstatesWithImages().map {
            DataResult.Success(it.map { estateWithImage ->
                estateWithImage.asEstate()
            })
        }.catch<DataResult<List<Estate>>> {
            emit(DataResult.Error(it))
        }
    }

    override fun getEstateWithDetails(estateId: String): DataResult<EstateWithDetails?> {
        return try {
            val result = estateDao.getEstateWithDetailsById(UUID.fromString(estateId))?.asEstateWithDetails()
            result?.let {
                DataResult.Success(it)
            } ?: DataResult.Error(NullPointerException("Estate with ID $estateId not found"))
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

}