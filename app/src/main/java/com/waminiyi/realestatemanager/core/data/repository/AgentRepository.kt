package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.DataResult

interface AgentRepository{
    suspend fun saveAgent(agent: Agent): DataResult<Unit>
    suspend fun getAllAgents(): DataResult<List<Agent>>
    suspend fun getAgent(agentUuid: String): DataResult<Agent?>
}