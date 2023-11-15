package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.Result

interface AgentRepository {
    suspend fun saveAgent(agent: Agent): Result<Unit>
    fun getAllAgents(): Result<List<Agent>>
    fun getAgent(agentUuid: String): Result<Agent?>
}