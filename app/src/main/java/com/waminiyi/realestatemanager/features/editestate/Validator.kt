package com.waminiyi.realestatemanager.features.editestate

import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.Status
import java.util.Date

class Validator {

    fun validate(uiState: EditEstateUiState): ValidationResult {
        return ValidationResult(
            validatePrice(uiState.price).successful &&
                    validateArea(uiState.area).successful &&
                    validateAddress(uiState.address).successful &&
                    validateRoomsCount(uiState.roomsCount).successful &&
                    validateAgent(uiState.agent).successful &&
                    validateFullDescription(uiState.fullDescription).successful &&
                    validateMainPhoto(uiState.mainPhoto).successful &&
                    validateMainPhotoDescription(uiState.mainPhotoDescription).successful &&
                    validateEntryDate(uiState.entryDate).successful &&
                    validateStatus(uiState.status).successful &&
                    validateType(uiState.type).successful
        )
    }

    private fun validateType(type: EstateType?): ValidationResult {
        return ValidationResult(successful = type != null)
    }

    private fun validatePrice(price: Int): ValidationResult {
        return ValidationResult(successful = price > 0)
    }

    private fun validateArea(area: Float): ValidationResult {
        return ValidationResult(successful = area > 0)
    }

    private fun validateRoomsCount(roomsCount: Int): ValidationResult {
        return ValidationResult(successful = roomsCount > 0)
    }

    private fun validateFullDescription(fullDescription: String): ValidationResult {
        return ValidationResult(successful = fullDescription.isNotBlank())
    }

    private fun validateMainPhotoDescription(description: String): ValidationResult {
        return ValidationResult(successful = description.isNotBlank())
    }

    private fun validateMainPhoto(mainPhoto: Photo?): ValidationResult {
        return ValidationResult(successful = mainPhoto != null)
    }


    private fun validateAddress(address: Address?): ValidationResult {
        return ValidationResult(successful = address != null)
    }

    private fun validateStatus(status: Status?): ValidationResult {
        return ValidationResult(successful = status != null)
    }

    private fun validateEntryDate(entryDate: Date?): ValidationResult {
        return ValidationResult(successful = entryDate != null)
    }

    private fun validateAgent(agent: Agent?): ValidationResult {
        return ValidationResult(successful = agent != null)
    }
}