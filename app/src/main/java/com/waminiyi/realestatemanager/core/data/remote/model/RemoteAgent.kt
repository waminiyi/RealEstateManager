package com.waminiyi.realestatemanager.core.data.remote.model

data class RemoteAgent(
    val agentUuid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val photoUrl: String
)
