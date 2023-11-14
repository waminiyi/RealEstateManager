package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.model.data.Agent
import java.util.*

/**
 * Class representing an agent in the real estate application.
 *
 * @property agentUuid The unique identifier (UUID) for the agent. Automatically generated if not provided.
 * @property firstName The first name of the agent.
 * @property lastName The last name of the agent.
 * @property email The email address of the agent.
 * @property phoneNumber The phone number of the agent.
 */
@Entity(tableName = "agents")
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
    val phoneNumber: String
)

fun Agent.asAgentEntity() = AgentEntity(
    agentUuid = UUID.fromString(this.uuid),
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    phoneNumber = this.phoneNumber
)