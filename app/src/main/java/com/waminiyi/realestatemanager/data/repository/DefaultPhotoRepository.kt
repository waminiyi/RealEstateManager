package com.waminiyi.realestatemanager.data.repository

import com.waminiyi.realestatemanager.data.database.dao.PhotoDao
import com.waminiyi.realestatemanager.data.database.model.asPhotoEntity
import com.waminiyi.realestatemanager.data.models.Result
import com.waminiyi.realestatemanager.data.models.Photo
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(
    private val photoDao: PhotoDao,
) : PhotoRepository {
    override suspend fun savePhoto(photo: Photo): Result<Unit> {
        return try {
            photoDao.upsertPhoto(photo.asPhotoEntity())
            Result.Success(Unit)
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    override fun getAllEstatePhotos(estateUuid: String): Result<List<Photo>> {
        return try {
            val photoEntities = photoDao.getPhotosByEstate(UUID.fromString(estateUuid))
            Result.Success(photoEntities.map { it.asPhoto() })
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    override fun getPhoto(photoUuid: String): Result<Photo> {
        return try {
            photoDao.getPhotoById(UUID.fromString(photoUuid))?.let {
                Result.Success(it.asPhoto())
            } ?: Result.Error(NullPointerException("Photo with uuid: $photoUuid not found"))
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    override suspend fun deletePhoto(photo: Photo): Result<Unit> {
        return try {
            photoDao.deletePhoto(photo.asPhotoEntity())
            Result.Success(Unit)
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    override suspend fun deletePhoto(photoId: String): Result<Unit> {
        return try {
            photoDao.deletePhoto(photoId)
            Result.Success(Unit)
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }
}