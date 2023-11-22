package com.waminiyi.realestatemanager.core.data.model

import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteAgent
import java.util.*

fun RemoteAgent.toAgentEntity() = AgentEntity(
    agentUuid = UUID.fromString(this.agentUuid),
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    phoneNumber = this.phoneNumber,
    photoUrl = this.photoUrl
)

fun AgentEntity.toRemoteAgent() = RemoteAgent(
    agentUuid = this.agentUuid.toString(),
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    phoneNumber = this.phoneNumber,
    photoUrl = this.photoUrl
)