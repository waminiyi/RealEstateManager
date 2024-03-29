package com.waminiyi.realestatemanager.core.domain.usecases

import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.data.repository.PhotoRepository
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import javax.inject.Inject

class AddEstateUseCase @Inject constructor(
    private val estateRepository: EstateRepository,
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(estateWithDetails: EstateWithDetails) {
        try {
            estateRepository.saveEstate(estateWithDetails)
            estateWithDetails.photos.forEach {
                photoRepository.savePhoto(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}