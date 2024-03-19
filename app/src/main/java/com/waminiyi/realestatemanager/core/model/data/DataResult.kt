package com.waminiyi.realestatemanager.core.model.data

sealed class DataResult<out R> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val exception: Throwable) : DataResult<Nothing>()
    data object Loading : DataResult<Nothing>()
}
