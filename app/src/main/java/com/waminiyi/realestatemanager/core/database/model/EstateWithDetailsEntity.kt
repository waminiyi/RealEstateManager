package com.waminiyi.realestatemanager.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails

/**
 * Class representing an estate with its associated details.
 *
 * @property estateEntity The embedded estate entity containing estate main information.
 * @property agentEntity The related agent entity representing the agent associated with the estate.
 * @property images The list of related image entities representing images associated with the estate.
 */
data class EstateWithDetailsEntity(
    @Embedded
    val estateEntity: EstateEntity,

    @Relation(
        parentColumn = "agent_id",
        entityColumn = "agent_uuid"
    )
    val agentEntity: AgentEntity,

    @Relation(
        parentColumn = "estate_uuid",
        entityColumn = "estate_uuid"
    )
    val images: List<PhotoEntity>

) {
    fun asEstateWithDetails() = EstateWithDetails(
        uuid = this.estateEntity.estateUuid.toString(),
        type = this.estateEntity.type,
        price = this.estateEntity.price,
        area = this.estateEntity.area,
        roomsCount = this.estateEntity.roomsCount,
        fullDescription = this.estateEntity.description,
        photos = this.images.map { it.asPhoto() },
        address = this.estateEntity.addressEntity.asAddress(),
        nearbyPointsOfInterest = this.estateEntity.poiList,
        estateStatus = this.estateEntity.estateStatus,
        entryDate = this.estateEntity.entryDate,
        saleDate = this.estateEntity.saleDate,
        agent = this.agentEntity.asAgent()
    )
}


