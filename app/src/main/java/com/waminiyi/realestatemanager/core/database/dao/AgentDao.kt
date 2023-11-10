package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.*
import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.database.model.AgentWithImage
import java.util.*

@Dao
interface AgentDao {
    @Upsert
    suspend fun upsertAgent(agentEntity: AgentEntity)

    @Transaction
    @Query("SELECT * FROM agents WHERE agent_uuid = :agentUuid")
    fun getAgent(agentUuid: UUID): AgentWithImage?

    @Transaction
    @Query("SELECT * FROM agents")
    fun getAllAgents(): List<AgentWithImage>
}