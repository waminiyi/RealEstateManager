package com.waminiyi.realestatemanager.core.domain.usecases

import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import javax.inject.Inject

class AddEstateUseCase @Inject constructor(private val estateRepository: EstateRepository) {
    suspend operator fun invoke(estateWithDetails: EstateWithDetails){
        estateRepository.saveEstate(estateWithDetails)
    }
}