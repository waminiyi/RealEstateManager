package com.waminiyi.realestatemanager.data.repository

import com.waminiyi.realestatemanager.data.models.Agent
import com.waminiyi.realestatemanager.data.models.Result

interface AgentRepository{
    suspend fun saveAgent(agent: Agent): Result<Unit>
    suspend fun getAllAgents(): Result<List<Agent>>
    suspend fun getAgent(agentUuid: String): Result<Agent?>
}