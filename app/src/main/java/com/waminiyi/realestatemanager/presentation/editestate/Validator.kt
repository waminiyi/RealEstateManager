package com.waminiyi.realestatemanager.presentation.editestate

import com.waminiyi.realestatemanager.data.models.Address
import com.waminiyi.realestatemanager.data.models.Agent
import com.waminiyi.realestatemanager.data.models.EstateStatus
import com.waminiyi.realestatemanager.data.models.EstateType
import com.waminiyi.realestatemanager.data.models.Photo
import java.util.Date

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

fun validateRoomsCount(
    totalRoomsCount: Int,
    bedroomsCount: Int,
) = when {
    totalRoomsCount >= bedroomsCount -> ValidationResult(true)
    else -> ValidationResult(false, "Ensure consistency between number of rooms")
}


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