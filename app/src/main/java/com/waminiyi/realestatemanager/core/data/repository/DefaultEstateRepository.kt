package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.data.datastore.model.VersionsList
import com.waminiyi.realestatemanager.core.data.model.toEstateEntity
import com.waminiyi.realestatemanager.core.data.model.toRemoteEstate
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteChange
import com.waminiyi.realestatemanager.core.data.remote.repository.RemoteDataRepository
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.LocalChangeDao
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import com.waminiyi.realestatemanager.core.database.model.asEstate
import com.waminiyi.realestatemanager.core.database.model.asEstateEntity
import com.waminiyi.realestatemanager.core.model.data.ClassTag
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.util.sync.Synchronizer
import com.waminiyi.realestatemanager.core.util.sync.changeLocalListSync
import com.waminiyi.realestatemanager.core.util.sync.changeRemoteListSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultEstateRepository @Inject constructor(
    private val estateDao: EstateDao,
    private val localChangeDao: LocalChangeDao,
    private val remoteDataRepository: RemoteDataRepository
) : EstateRepository {
    override suspend fun saveEstate(estateWithDetails: EstateWithDetails): DataResult<Unit> {
        return try {
            estateDao.upsertEstate(estateWithDetails.asEstateEntity())
            localChangeDao.upsertChange(LocalChangeEntity(estateWithDetails.uuid, ClassTag.Estate, false))
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

    override suspend fun getEstatesToUpload(): List<EstateEntity> =
        estateDao.getEstatesByIds(
            localChangeDao.getChangesByClassTag(ClassTag.Estate)
                .map { change ->
                    UUID.fromString(change.id)
                })

    override suspend fun syncFromRemoteWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.changeLocalListSync(
            currentLocalVersionReader = VersionsList::estateVersion,
            remoteChangeListFetcher = { currentVersion -> remoteDataRepository.getEstatesChangeList(currentVersion) },
            localVersionUpdater = { latestVersion -> copy(estateVersion = latestVersion) },
            localModelUpdater = { changedIds ->
                changedIds.forEach { id ->
                    remoteDataRepository.getEstate(id)?.let { estateDao.upsertEstate(it.toEstateEntity()) }
                }
            }
        )
    }

    override suspend fun syncToRemoteWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.changeRemoteListSync(
            localChangesFetcher = { localChangeDao.getChangesByClassTag(ClassTag.Estate) },
            localVersionUpdater = { latestVersion -> copy(estateVersion = latestVersion) },
            remoteVersionUpdater = { localChanges, latestVersion ->
                localChanges.map {
                    RemoteChange(it.id, it.classTag.name, latestVersion, it.isDeleted)
                }
            },
            remoteModelUpdater = { changedIds ->
                estateDao.getEstatesByIds(changedIds.map { UUID.fromString(it) }).forEach { estateEntity ->
                    remoteDataRepository.uploadEstate(estateEntity.toRemoteEstate())
                }
            }
        )
    }

}