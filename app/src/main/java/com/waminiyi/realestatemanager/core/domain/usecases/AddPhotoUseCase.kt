package com.waminiyi.realestatemanager.core.domain.usecases

import com.waminiyi.realestatemanager.core.data.repository.MediaFileRepository
import com.waminiyi.realestatemanager.core.data.repository.PhotoRepository
import com.waminiyi.realestatemanager.core.model.data.Photo
import javax.inject.Inject

class AddPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val mediaFileRepository: MediaFileRepository,
) {
    suspend operator fun invoke(photo: Photo) {
    }
}