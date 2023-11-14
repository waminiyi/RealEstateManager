package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.ImageDao
import com.waminiyi.realestatemanager.core.database.model.asAgentEntity
import com.waminiyi.realestatemanager.core.database.model.asImageEntity
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.WorkResult
import java.io.IOException
import java.util.*
import javax.inject.Inject

class DefaultAgentRepository @Inject constructor(
    private val agentDao: AgentDao,
    private val imageDao: ImageDao
) : AgentRepository {
    override suspend fun saveAgent(agent: Agent): WorkResult<Unit> {
        return try {
            agentDao.upsertAgent(agent.asAgentEntity())
            imageDao.upsertImage(agent.image.asImageEntity(agent.uuid))
            WorkResult.Success(Unit)
        } catch (exception: IOException) {
            WorkResult.Error(exception)
        }
    }

    override fun getAllAgents(): WorkResult<List<Agent>> {
        return try {
            val result = agentDao.getAllAgents().map {
                it.asAgent()
            }
            WorkResult.Success(result)
        } catch (exception: IOException) {
            WorkResult.Error(exception)
        }
    }

    override fun getAgent(agentUuid: String): WorkResult<Agent?> {
        return try {
            val result = agentDao.getAgent(UUID.fromString(agentUuid))?.asAgent()
            result?.let {
                WorkResult.Success(it)
            } ?: WorkResult.Error(NullPointerException("Agent with ID $agentUuid not found"))
        } catch (exception: IOException) {
            WorkResult.Error(exception)
        }
    }

}