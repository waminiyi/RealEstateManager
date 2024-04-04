package com.waminiyi.realestatemanager.features.editEstate.models

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
