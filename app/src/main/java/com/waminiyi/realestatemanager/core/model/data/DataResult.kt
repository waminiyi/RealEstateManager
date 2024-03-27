package com.waminiyi.realestatemanager.core.model.data

/**
 * Sealed class representing a result of an operation that can be either success, error, or loading.
 * @param R The type of data returned upon success.
 */
sealed class DataResult<out R> {

    /**
     * Represents a successful result containing data of type [T].
     * @param T The type of data contained in the success result.
     * @property data The data contained in the success result.
     */
    data class Success<out T>(val data: T) : DataResult<T>()

    /**
     * Represents an error result containing an exception.
     * @param exception The exception associated with the error result.
     */
    data class Error(val exception: Throwable) : DataResult<Nothing>()

    /**
     * Represents a loading result indicating that the operation is in progress.
     * This object does not contain any data.
     */
    data object Loading : DataResult<Nothing>()
}
