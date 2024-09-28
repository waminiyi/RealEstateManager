package com.waminiyi.realestatemanager.data.repository

import com.waminiyi.realestatemanager.data.database.dao.AgentDao
import com.waminiyi.realestatemanager.data.database.model.asAgentEntity
import com.waminiyi.realestatemanager.data.models.Agent
import com.waminiyi.realestatemanager.data.models.Result
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultAgentRepository @Inject constructor(
    private val agentDao: AgentDao,
) : AgentRepository {
    override suspend fun saveAgent(agent: Agent): Result<Unit> {
        return try {
            agentDao.upsertAgent(agent.asAgentEntity())
            Result.Success(Unit)
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    override suspend fun getAllAgents(): Result<List<Agent>> {
        return try {
            val result = agentDao.getAllAgents().map {
                it.asAgent()
            }
            Result.Success(result)
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    override suspend fun getAgent(agentUuid: String): Result<Agent?> {
        return try {
            val result = agentDao.getAgent(UUID.fromString(agentUuid))?.asAgent()
            result?.let {
                Result.Success(it)
            } ?: Result.Error(NullPointerException("Agent with ID $agentUuid not found"))
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }
}