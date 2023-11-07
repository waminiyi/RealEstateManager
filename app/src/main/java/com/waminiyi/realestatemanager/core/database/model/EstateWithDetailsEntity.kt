package com.waminiyi.realestatemanager.core.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Class representing an estate with its associated details.
 *
 * @property estateEntity The embedded estate entity containing estate main information.
 * @property agentEntity The related agent entity representing the agent associated with the estate.
 * @property images The list of related image entities representing images associated with the estate.
 * @property facilities The list of related facility entities associated with the estate.
 * @property pointsOfInterest The list of related point of interest entities associated with the estate.
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
        entityColumn = "owner_uuid"
    )
    val images: List<ImageEntity>,

    @Relation(
        parentColumn = "estate_uuid",
        entityColumn = "facility_id",
        associateBy = Junction(EstateAndFacilityLink::class)
    )
    val facilities: List<FacilityEntity>,

    @Relation(
        parentColumn = "estate_uuid",
        entityColumn = "poi_id",
        associateBy = Junction(EstateAndPoiLink::class)
    )
    val pointsOfInterest: List<PointOfInterestEntity>
)

