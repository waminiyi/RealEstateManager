package com.waminiyi.realestatemanager.core.data.datastore.model

import java.sql.Timestamp


/**
 * Class summarizing the local version of each model for sync
 */
data class LastCommit(
    val id: String,
    val timestamp: Timestamp
)