package com.waminiyi.realestatemanager.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class representing an agent along with their associated image .
 *
 * @property agentEntity The [AgentEntity] representing the agent's details.
 * @property imageEntity The [ImageEntity] representing the agent's associated image.
 */
data class AgentWithImage(
    @Embedded val agentEntity: AgentEntity,
    @Relation(
        parentColumn = "agent_uuid",
        entityColumn = "owner_uuid"
    )
    val imageEntity: ImageEntity
)