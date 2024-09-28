package com.waminiyi.realestatemanager.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.Constants.AGENTS_TABLE_NAME
import com.waminiyi.realestatemanager.data.models.Agent
import java.util.UUID

/**
 * Class representing an agent in the real estate application.
 *
 * This class encapsulates information about a real estate agent, including their unique identifier,
 * first name, last name, email address, phone number, photo URL, and upload status.
 *
 * @property agentUuid The unique identifier (UUID) for the agent. Automatically generated if not provided.
 * @property firstName The first name of the agent.
 * @property lastName The last name of the agent.
 * @property email The email address of the agent.
 * @property phoneNumber The phone number of the agent.
 * @property photoUrl The URL of the agent's photo.
 */
@Entity(tableName = AGENTS_TABLE_NAME)
data class AgentEntity(
    @PrimaryKey
    @ColumnInfo(name = "agent_uuid")
    val agentUuid: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String,
) {
    fun asAgent() = Agent(
        uuid = this.agentUuid.toString(),
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        phoneNumber = this.phoneNumber,
        photoUrl = this.photoUrl
    )
}

fun Agent.asAgentEntity() = AgentEntity(
    agentUuid = UUID.fromString(this.uuid),
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    phoneNumber = this.phoneNumber,
    photoUrl = this.photoUrl
)