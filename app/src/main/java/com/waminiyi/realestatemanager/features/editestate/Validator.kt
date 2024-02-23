package com.waminiyi.realestatemanager.features.editestate

import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Photo
import java.util.Date


fun validate(uiState: EditEstateUiState): ValidationResult {
    //TODO: move validation logic from viewmodel to here
    return ValidationResult(
        validatePrice(uiState.price).successful &&
                validateArea(uiState.area).successful &&
                validateAddress(uiState.address).successful &&
//                validateTotalRoomsCount(uiState.roomsCount).successful &&
                validateAgent(uiState.agent).successful &&
//                validateFullDescription(uiState.fullDescription).successful &&
                validatePhotos(uiState.photos).successful &&
//                validateEntryDate(uiState.entryDate).successful &&
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
        ValidationResult(false, "Price must be greater than 0")
    }
}

fun validateArea(area: Int?): ValidationResult {
    return if (area != null && area > 0) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Area value must greater than 0")
    }
}

//fun validateTotalRoomsCount(
//    totalRoomsCount: Int?,
//): ValidationResult {
//    return when (totalRoomsCount) {
//        null -> ValidationResult(false, "Enter the total number of rooms")
//        else -> ValidationResult(true)
//    }
//}

fun validateRoomsCount(
    totalRoomsCount: Int,
    bedroomsCount: Int,
) = when {
    totalRoomsCount >= bedroomsCount -> ValidationResult(true)
    else -> ValidationResult(false, "Ensure consistency between number of rooms")
}

//fun validateBedroomsCount(roomsCount: Int?): ValidationResult {
//    return if (roomsCount != null && roomsCount > 0) {
//        ValidationResult(true)
//    } else {
//        ValidationResult(false, "Enter the total number of bedrooms")
//    }
//}

//fun validateBathroomsCount(roomsCount: Int?): ValidationResult {
//    return if (roomsCount != null && roomsCount > 0) {
//        ValidationResult(true)
//    } else {
//        ValidationResult(false, "Enter the number of bathrooms")
//    }
//}

//fun validateFullDescription(fullDescription: String): ValidationResult {
//    return if (fullDescription.isNotBlank()) {
//        ValidationResult(true)
//    } else {
//        ValidationResult(false, "Please provide a description for the estate")
//    }
//}

fun validatePhotos(photos: List<Photo>): ValidationResult {
    return if (photos.isEmpty()) {
        ValidationResult(false, "Please select at least one photo")
    } else {
        if (photos.any { it.description.isNullOrBlank() }) {
            ValidationResult(false, "Please add description for each photo")
        } else {
            ValidationResult(true)
        }
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

//fun validateEntryDate(entryDate: Date?): ValidationResult {
//    return if (entryDate != null) {
//        ValidationResult(true)
//    } else {
//        ValidationResult(false, "Please select the estate's entry date")
//    }
//}

fun validateDates(entryDate: Date?, saleDate: Date?): ValidationResult {
    return when {
        entryDate != null && saleDate != null -> {
            if (entryDate.after(saleDate)) {
                ValidationResult(false, "Ensure consistency between dates")
            } else {
                ValidationResult(true)
            }
        }

        else -> ValidationResult(true)
    }
}

fun validateStatus(status: EstateStatus, saleDate: Date?): ValidationResult {
    return when {
        status == EstateStatus.AVAILABLE && saleDate == null -> ValidationResult(true)
        status == EstateStatus.SOLD && saleDate != null -> ValidationResult(true)
        else -> ValidationResult(false, "No consistency between status and sale date")
    }
}

fun validateAgent(agent: Agent?): ValidationResult {
    return if (agent != null) {
        ValidationResult(true)
    } else {
        ValidationResult(false, "Please select an agent")
    }
}