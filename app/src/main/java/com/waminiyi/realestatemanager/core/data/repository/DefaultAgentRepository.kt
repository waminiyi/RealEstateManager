package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.model.asAgentEntity
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.Result
import java.io.IOException
import java.util.*
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

    override fun getAllAgents(): Result<List<Agent>> {
        return try {
            val result = agentDao.getAllAgents().map {
                it.asAgent()
            }
            Result.Success(result)
        } catch (exception: IOException) {
            Result.Error(exception)
        }
    }

    override fun getAgent(agentUuid: String): Result<Agent?> {
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