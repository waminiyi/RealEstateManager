package com.waminiyi.realestatemanager.core.data.remote.model

/**
 * Network representation of a change list for a model.
 *
 * Change lists are a representation of a server-side map like data structure of model ids to
 * metadata about that model. In a single change list, a given model id can only show up once.
 */
data class RemoteChange(
    /**
     * The id of the object that was changed
     */
    val id: String,

    /**
     * The unique tag of the object class name
     */
    val tag: String,

    /**
     * Change timestamp
     */
    val timestamp: Long,
    /**
     * Summarizes the update to the model; whether it was deleted or updated.
     * Updates include creations.
     */
    @field:JvmField
    val isDelete: Boolean,
)