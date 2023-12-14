package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.data.datastore.model.VersionsList
import com.waminiyi.realestatemanager.core.data.model.toPhotoEntity
import com.waminiyi.realestatemanager.core.data.remote.repository.RemoteDataRepository
import com.waminiyi.realestatemanager.core.database.dao.LocalChangeDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.database.model.asPhotoEntity
import com.waminiyi.realestatemanager.core.model.data.ClassTag
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.RegistrationStatus
import com.waminiyi.realestatemanager.core.util.sync.Synchronizer
import com.waminiyi.realestatemanager.core.util.sync.changeLocalListSync
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(
    private val photoDao: PhotoDao,
    private val mediaFileRepository: MediaFileRepository,
    private val localChangeDao: LocalChangeDao,
    private val remoteDataRepository: RemoteDataRepository
) : PhotoRepository {
    override suspend fun savePhoto(photo: Photo): DataResult<Unit> {
        return try {
            val photoInternalUri = mediaFileRepository.savePhotoFileToInternalStorage(photo.localUri, photo.uuid)
            photoInternalUri?.let    {
                photoDao.upsertPhoto(photo.copy())
            }


            localChangeDao.upsertChange(LocalChangeEntity(photo.uuid, ClassTag.PhotoFile, false))
            DataResult.Success(Unit)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override fun getAllEstatePhotos(estateUuid: String): DataResult<List<Photo>> {
        return try {
            val photoEntities = photoDao.getPhotosByEstate(UUID.fromString(estateUuid))
            DataResult.Success(photoEntities.map { it.asPhoto() })
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override fun getPhoto(photoUuid: String): DataResult<Photo> {
        return try {
            photoDao.getPhotoById(UUID.fromString(photoUuid))?.let {
                DataResult.Success(it.asPhoto())
            } ?: DataResult.Error(NullPointerException("Photo with uuid: $photoUuid not found"))
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override suspend fun deletePhoto(photo: Photo): DataResult<Unit> {
        return try {
            photoDao.deletePhoto(photo.asPhotoEntity())
            DataResult.Success(Unit)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override suspend fun getPhotosToUpload(): List<PhotoEntity> =
        photoDao.getPhotosWithUploadStatus(RegistrationStatus.OnGoing)

    override suspend fun syncFromRemoteWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.changeLocalListSync(
            currentLocalVersionReader = VersionsList::photoVersion,
            remoteChangeListFetcher = { currentVersion -> remoteDataRepository.getPhotosChangeList(currentVersion) },
            localVersionUpdater = { latestVersion -> copy(photoVersion = latestVersion) },
            localModelUpdater = { changedIds ->
                changedIds.forEach { id ->
                    remoteDataRepository.getPhoto(id)?.let { photoDao.upsertPhoto(it.toPhotoEntity()) }
                }
            }
        )
    }

    override suspend fun syncToRemoteWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }

    suspend fun savePhotoFileToInternalStorage(inputUri: String): Pair<String, String>? =
        mediaFileRepository.savePhotoFileToInternalStorage(inputUri)

    suspend fun deletePhotoFileFromInternalStorage(photoUuid: String): Boolean =
        mediaFileRepository.deletePhotoFileFromInternalStorage(photoUuid)
}