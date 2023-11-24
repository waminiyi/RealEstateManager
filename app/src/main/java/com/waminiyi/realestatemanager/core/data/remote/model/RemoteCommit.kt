package com.waminiyi.realestatemanager.core.data.remote.model

import java.sql.Timestamp

data class RemoteCommit(
    val commitId: String,
    val remoteChanges: List<RemoteChange>,
    val timestamp: Timestamp,
    val authorId:String
)