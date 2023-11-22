package com.waminiyi.realestatemanager.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.waminiyi.realestatemanager.core.model.data.Agent

/**
 * Data class representing an agent along with their associated image .
 *
 * @property agentEntity The [AgentEntity] representing the agent's details.
 * @property photoEntity The [PhotoEntity] representing the agent's associated image.
 */
data class AgentWithImage(
    @Embedded val agentEntity: AgentEntity,
    @Relation(
        parentColumn = "agent_uuid",
        entityColumn = "estate_uuid"
    )
    val photoEntity: PhotoEntity
) {
    fun asAgent() = Agent(
        uuid = this.agentEntity.agentUuid.toString(),
        firstName = this.agentEntity.firstName,
        lastName = this.agentEntity.lastName,
        email = this.agentEntity.email,
        phoneNumber = this.agentEntity.phoneNumber,
        photoUrl = this.agentEntity.photoUrl
    )
}

//fun Agent.asAgentWithImage() = AgentWithImage(
//    agentEntity = AgentEntity(
//        agentUuid = UUID.fromString(this.uuid),
//        firstName = this.firstName,
//        lastName = this.lastName,
//        email = this.email,
//        phoneNumber = this.phoneNumber,
//        photoUrl=this.photoUrl
//    ),
//    imageEntity = this.image.asImageEntity(this.uuid)
//)

