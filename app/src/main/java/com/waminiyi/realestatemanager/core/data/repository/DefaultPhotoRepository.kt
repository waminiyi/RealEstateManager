package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Photo
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(private val photoDao: PhotoDao) : PhotoRepository {
    override suspend fun savePhoto(photo: Photo): DataResult<Unit> {
        TODO("Not yet implemented")
    }

    override fun getAllEstatePhotos(estateUuid: String): DataResult<List<Photo>> {
        TODO("Not yet implemented")
    }

    override fun getPhoto(photoUuid: String): DataResult<Photo?> {
        TODO("Not yet implemented")
    }

    override fun deletePhoto(photo: Photo): DataResult<Unit> {
        TODO("Not yet implemented")
    }
}