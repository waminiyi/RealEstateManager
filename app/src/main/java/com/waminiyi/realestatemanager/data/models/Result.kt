package com.waminiyi.realestatemanager.data.models

/**
 * Sealed class representing a result of an operation that can be either success, error, or loading.
 * @param R The type of data returned upon success.
 */
sealed class Result<out R> {

    /**
     * Represents a successful result containing data of type [T].
     * @param T The type of data contained in the success result.
     * @property data The data contained in the success result.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents an error result containing an exception.
     * @param exception The exception associated with the error result.
     */
    data class Error(val exception: Throwable) : Result<Nothing>()

    /**
     * Represents a loading result indicating that the operation is in progress.
     * This object does not contain any data.
     */
    data object Loading : Result<Nothing>()
}
