package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.database.model.asPhotoEntity
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Photo
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(
    private val photoDao: PhotoDao,
) : PhotoRepository {
    override suspend fun savePhoto(photo: Photo): DataResult<Unit> {
        return try {
            photoDao.upsertPhoto(photo.asPhotoEntity())
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

    override suspend fun deletePhoto(photoId: String): DataResult<Unit> {
        return try {
            photoDao.deletePhoto(photoId)
            DataResult.Success(Unit)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }
}