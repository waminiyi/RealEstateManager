package com.waminiyi.realestatemanager.core.data.datastore.model

import com.waminiyi.realestatemanager.core.data.remote.model.RemoteCommit
import java.sql.Timestamp


/**
 * Class summarizing the local version of each model for sync
 */
data class LastCommit(
    val id: String,
    val timestamp: Timestamp
) {
    companion object {
        // Define a constant to represent the installation commit
        val INSTALLATION_COMMIT = LastCommit("installation_commit_id", Timestamp(0))
    }
}

fun RemoteCommit.toLastCommit() = LastCommit(this.commitId, this.timestamp)