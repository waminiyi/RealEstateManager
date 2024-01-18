package com.waminiyi.realestatemanager.features.editestate

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
