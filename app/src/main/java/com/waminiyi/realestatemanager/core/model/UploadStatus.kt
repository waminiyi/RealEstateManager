package com.waminiyi.realestatemanager.core.model

/**
 * Represents the status of an item to be uploaded.
 */
enum class UploadStatus {
    /** Indicates that the item is missing information. */
    ToUpload,

    /** Indicates that the item has been successfully uploaded. */
    Uploaded,
}