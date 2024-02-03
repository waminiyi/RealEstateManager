package com.waminiyi.realestatemanager.features.editestate

import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import java.util.Date


fun validate(uiState: EditEstateUiState): ValidationResult {
    return ValidationResult(
        validatePrice(uiState.price).successful &&
                validateArea(uiState.area).successful &&
                validateAddress(uiState.address).successful &&
                validateRoomsCount(uiState.roomsCount).successful &&
                validateAgent(uiState.agent).successful &&
                validateFullDescription(uiState.fullDescription).successful &&
                validateMainPhoto(uiState.photos).successful &&
                validateMainPhotoDescription(uiState.mainPhotoDescription).successful &&
                validateEntryDate(uiState.entryDate).successful &&
                validateStatus(uiState.estateStatus).successful &&
                validateType(uiState.type).successful
    )
}

fun validateType(type: EstateType?): ValidationResult {
    return if (type != null) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Please select a type")
    }
}

fun validatePrice(price: Int?): ValidationResult {
    return if (price != null && price > 0) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "You must enter a price greater than 0")
    }
}

fun validateArea(area: Int?): ValidationResult {
    return if (area != null && area > 0) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "You must enter an area value greater than 0")
    }
}

fun validateRoomsCount(roomsCount: Int?): ValidationResult {
    return if (roomsCount != null && roomsCount > 0) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "You must enter a number of rooms greater than 0")
    }
}

fun validateFullDescription(fullDescription: String): ValidationResult {
    return if (fullDescription.isNotBlank()) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Please provide a description for the estate")
    }
}

fun validateMainPhotoDescription(description: String): ValidationResult {
    return if (description.isNotBlank()) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Please provide a description for the main photo")
    }
}

fun validateMainPhoto(photos: List<Photo>): ValidationResult {
    return if (photos.isNotEmpty()) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Please select at least one photo")
    }
}


fun validateAddress(address: Address?): ValidationResult {
    return if (address != null) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Please select the estate's address")
    }
}

fun validateStatus(estateStatus: EstateStatus?): ValidationResult {
    return ValidationResult(successful = estateStatus != null)
}

fun validateEntryDate(entryDate: Date?): ValidationResult {
    return if (entryDate != null) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Please select the estate's entry date")
    }
}

fun validateAgent(agent: Agent?): ValidationResult {
    return if (agent != null) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Please select an agent")
    }
}