package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.WorkResult

interface AgentRepository {
    suspend fun saveAgent(agent: Agent): WorkResult<Unit>
    fun getAllAgents(): WorkResult<List<Agent>>
    fun getAgent(agentUuid: String): WorkResult<Agent?>
}