package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.model.asAgentEntity
import com.waminiyi.realestatemanager.core.model.Agent
import com.waminiyi.realestatemanager.core.model.DataResult
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultAgentRepository @Inject constructor(
    private val agentDao: AgentDao,
) : AgentRepository {
    override suspend fun saveAgent(agent: Agent): DataResult<Unit> {
        return try {
            agentDao.upsertAgent(agent.asAgentEntity())
            DataResult.Success(Unit)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override suspend fun getAllAgents(): DataResult<List<Agent>> {
        return try {
            val result = agentDao.getAllAgents().map {
                it.asAgent()
            }
            DataResult.Success(result)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override suspend fun getAgent(agentUuid: String): DataResult<Agent?> {
        return try {
            val result = agentDao.getAgent(UUID.fromString(agentUuid))?.asAgent()
            result?.let {
                DataResult.Success(it)
            } ?: DataResult.Error(NullPointerException("Agent with ID $agentUuid not found"))
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }
}