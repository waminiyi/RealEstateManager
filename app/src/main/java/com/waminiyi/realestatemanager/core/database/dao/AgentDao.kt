package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import java.util.UUID

@Dao
interface AgentDao {
    @Upsert
    suspend fun upsertAgent(agentEntity: AgentEntity)

    @Transaction
    @Query("SELECT * FROM agents WHERE agent_uuid = :agentUuid")
    suspend fun getAgent(agentUuid: UUID): AgentEntity?

    @Transaction
    @Query("SELECT * FROM agents")
    suspend fun getAllAgents(): List<AgentEntity>

    @Transaction
    @Query("SELECT * FROM agents WHERE agent_uuid IN (:agentUuids)")
    fun getAgentsByIds(agentUuids: List<UUID>): List<AgentEntity>
}