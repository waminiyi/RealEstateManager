package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.util.sync.Syncable

interface AgentRepository : Syncable {
    suspend fun saveAgent(agent: Agent): DataResult<Unit>
    suspend fun getAllAgents(): DataResult<List<Agent>>
    suspend fun getAgent(agentUuid: String): DataResult<Agent?>
    suspend  fun getAgentsToUpload(): List<AgentEntity>
}