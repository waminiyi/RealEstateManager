package com.waminiyi.realestatemanager.data.repository

import com.waminiyi.realestatemanager.data.database.dao.EstateDao
import com.waminiyi.realestatemanager.data.database.model.asEstate
import com.waminiyi.realestatemanager.data.database.model.asEstateEntity
import com.waminiyi.realestatemanager.data.models.Result
import com.waminiyi.realestatemanager.data.models.Estate
import com.waminiyi.realestatemanager.data.models.EstateWithDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultEstateRepository @Inject constructor(
    private val estateDao: EstateDao,
    private val filterRepository: FilterRepository
) : EstateRepository {
    override suspend fun saveEstate(estateWithDetails: EstateWithDetails): Result<Unit> {
        return try {
            estateDao.upsertEstate(estateWithDetails.asEstateEntity())
            Result.Success(Unit)
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllEstatesStream(): Flow<Result<List<Estate>>> {

        return filterRepository.filter.flatMapLatest { filter ->
            val param = filter.asQueryParameter()
            estateDao.getAllEstatesWithImages(
                minPrice = param.minPrice,
                maxPrice = param.maxPrice,
                minArea = param.minArea,
                maxArea = param.maxArea,
                typesIsEmpty = param.estateTypes.isNullOrEmpty(),
                types = param.estateTypes ?: emptyList(),
                citiesIsEmpty = param.cities.isNullOrEmpty(),
                cities = param.cities,
                roomsIsEmpty = param.roomsCounts.isNullOrEmpty(),
                roomsCounts = param.roomsCounts ?: emptyList(),
                roomsCountThreshold = param.roomsCountThreshold,
                bedroomsIsEmpty = param.bedroomsCounts.isNullOrEmpty(),
                bedroomsCounts = param.bedroomsCounts ?: emptyList(),
                bedroomsCountThreshold = param.bedroomsCountThreshold,
                photoMinimumCount = param.photosMinimalCount,
                estateStatus = param.estateStatus,
                addedAfter = param.addedAfter,
                soldAfter = param.soldAfter,
                poi = param.pointOfInterest?.toString()
            ).map { estateListWithImages ->
                Result.Success(estateListWithImages.map { estateWithImage ->
                    estateWithImage.asEstate()
                })
            }.catch<Result<List<Estate>>> {
                emit(Result.Error(it))
            }
        }
    }

    override suspend fun getEstateWithDetails(estateId: String): Result<EstateWithDetails?> {
        return try {
            val result =
                estateDao.getEstateWithDetailsById(UUID.fromString(estateId))?.asEstateWithDetails()
            result?.let {
                Result.Success(it)
            } ?: Result.Error(NullPointerException("Estate with ID $estateId not found"))
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }
}