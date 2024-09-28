package com.waminiyi.realestatemanager.presentation.editestate

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
