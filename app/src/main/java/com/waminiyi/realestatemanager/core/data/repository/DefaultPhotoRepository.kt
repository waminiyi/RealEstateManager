package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.data.datastore.model.VersionsList
import com.waminiyi.realestatemanager.core.data.extension.toPhotoEntity
import com.waminiyi.realestatemanager.core.data.extension.toRemotePhoto
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteChange
import com.waminiyi.realestatemanager.core.data.remote.repository.RemoteDataRepository
import com.waminiyi.realestatemanager.core.database.dao.LocalChangeDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.database.model.asPhotoEntity
import com.waminiyi.realestatemanager.core.model.data.ClassTag
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.util.sync.Synchronizer
import com.waminiyi.realestatemanager.core.util.sync.changeLocalListSync
import com.waminiyi.realestatemanager.core.util.sync.changeRemoteListSync
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
            photoDao.upsertPhoto(photo.asPhotoEntity())
            DataResult.Success(Unit)
        } catch (exception: IOException) {
            exception.printStackTrace()
            DataResult.Error(exception)
            throw exception
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

    override suspend fun getPhotosToUpload(): List<PhotoEntity> = photoDao.getPhotosByIds(
        localChangeDao.getChangesByClassTag(ClassTag.Photo).map { change ->
            UUID.fromString(change.id)
        })


    override suspend fun syncFromRemoteWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.changeLocalListSync(
            currentLocalVersionReader = VersionsList::photoVersion,
            remoteChangeListFetcher = { currentVersion -> remoteDataRepository.getPhotosChangeList(currentVersion) },
            localVersionUpdater = { latestVersion -> copy(photoVersion = latestVersion) },
            localModelUpdater = { changedIds ->
                changedIds.forEach { syncPhotoFromRemote(it) }
            },
            localModelDeleter = { changeIds ->
                changeIds.forEach { deletePhotoModel(it) }
            }
        )
    }

    override suspend fun syncToRemoteWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.changeRemoteListSync(
            localChangesFetcher = { localChangeDao.getChangesByClassTag(ClassTag.Photo) },
            localVersionUpdater = { latestVersion -> copy(photoVersion = latestVersion) },
            remoteVersionUpdater = { localChanges, latestVersion ->
                localChanges.map {
                    RemoteChange(it.id, it.classTag.name, latestVersion, it.isDeleted)
                }
            },
            remoteModelDeleter = { changedIds ->
                changedIds.forEach { deleteRemotePhotoModel(it) }
            },
            remoteModelUpdater = { changedIds ->
                changedIds.forEach { syncPhotoToRemote(it) }
            }
        )
    }

    override suspend fun syncPhotoToRemote(photoId: String): DataResult<Unit> {
        return try {
            // Retrieve the local photo from the database
            val localPhoto = photoDao.getPhotoById(UUID.fromString(photoId))
                ?: return DataResult.Error(NullPointerException("Photo with id: $photoId not found"))

            // Upload the photo to Remote Storage
            val remoteUrl = mediaFileRepository.uploadPhotoFile(localPhoto.localPath)
                ?: return DataResult.Error(IOException("Failed to upload photo to Firebase Storage"))

            // Update the local photo entity with the remote URL
            val updatedPhoto = localPhoto.copy(url = remoteUrl)
            photoDao.upsertPhoto(updatedPhoto)

            // Upload the updated photo object to Remote
            remoteDataRepository.uploadPhoto(updatedPhoto.toRemotePhoto())
            localChangeDao.deleteChange(photoId)

            DataResult.Success(Unit)
        } catch (exception: Exception) {
            DataResult.Error(exception)
        }
    }

    override suspend fun syncPhotoFromRemote(photoId: String): DataResult<Unit> {
        return try {
            // Fetch the photo object from remote
            val remotePhoto = remoteDataRepository.getPhoto(photoId)
                ?: return DataResult.Error(NullPointerException("Photo with id: $photoId not found on Firebase"))

            // Save the remote photo to the local database
            photoDao.upsertPhoto(remotePhoto.toPhotoEntity())

            DataResult.Success(Unit)
        } catch (exception: Exception) {
            DataResult.Error(exception)
        }
    }

    private suspend fun deletePhotoModel(photoId: String) {
        photoDao.deletePhoto(photoId)
        mediaFileRepository.deletePhotoFileFromInternalStorage(photoId)
    }

    private suspend fun deleteRemotePhotoModel(photoId: String) {
        photoDao.getPhotoById(UUID.fromString(photoId))?.let {
            remoteDataRepository.deletePhoto(photoId)
            mediaFileRepository.deletePhotoFromRemote(it.url)
        }
    }
}