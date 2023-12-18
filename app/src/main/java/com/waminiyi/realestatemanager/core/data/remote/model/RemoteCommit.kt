package com.waminiyi.realestatemanager.core.data.remote.model

data class RemoteCommit(
    val commitId: String,
    val remoteChanges: List<RemoteChange>,
    val timestamp: Long,
    val authorId: String
)