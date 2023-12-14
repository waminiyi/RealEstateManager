package com.waminiyi.realestatemanager.core.domain.usecases

import com.waminiyi.realestatemanager.core.data.repository.MediaFileRepository
import com.waminiyi.realestatemanager.core.data.repository.PhotoRepository
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import javax.inject.Inject

class AddPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val mediaFileRepository: MediaFileRepository,
) {
    suspend operator fun invoke(estateWithDetails: EstateWithDetails) {
        estateRepository.saveEstate(estateWithDetails)
    }

    suspend fun savePhotoFileToInternalStorage(inputUri: String, outputPhotoUuid: String): String? =
        mediaFileRepository.savePhotoFileToInternalStorage(
            inputUri, outputPhotoUuid
        )
}