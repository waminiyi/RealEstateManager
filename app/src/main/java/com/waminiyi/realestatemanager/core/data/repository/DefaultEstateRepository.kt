package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.model.asEstate
import com.waminiyi.realestatemanager.core.database.model.asEstateEntity
import com.waminiyi.realestatemanager.core.model.DataResult
import com.waminiyi.realestatemanager.core.model.Estate
import com.waminiyi.realestatemanager.core.model.EstateWithDetails
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
    override suspend fun saveEstate(estateWithDetails: EstateWithDetails): DataResult<Unit> {
        return try {
            estateDao.upsertEstate(estateWithDetails.asEstateEntity())
            DataResult.Success(Unit)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllEstatesStream(): Flow<DataResult<List<Estate>>> {

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
                DataResult.Success(estateListWithImages.map { estateWithImage ->
                    estateWithImage.asEstate()
                })
            }.catch<DataResult<List<Estate>>> {
                emit(DataResult.Error(it))
            }
        }
    }

    override suspend fun getEstateWithDetails(estateId: String): DataResult<EstateWithDetails?> {
        return try {
            val result =
                estateDao.getEstateWithDetailsById(UUID.fromString(estateId))?.asEstateWithDetails()
            result?.let {
                DataResult.Success(it)
            } ?: DataResult.Error(NullPointerException("Estate with ID $estateId not found"))
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }
}