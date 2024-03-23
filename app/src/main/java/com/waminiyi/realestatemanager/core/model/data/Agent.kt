package com.waminiyi.realestatemanager.core.model.data

/**
 * Data class representing an agent.
 * @property uuid The unique identifier of the agent.
 * @property firstName The first name of the agent.
 * @property lastName The last name of the agent.
 * @property email The email address of the agent.
 * @property phoneNumber The phone number of the agent.
 * @property photoUrl The URL to the photo of the agent.
 * @property roles The roles assigned to the agent. By default, the agent is assigned the role 'AGENT'.
 */
data class Agent(
    val uuid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val photoUrl: String,
    val roles: Set<Role> = setOf(Role.AGENT)
)

/**
 * Enumeration class representing roles.
 * Roles can be ADMIN or AGENT.
 */
enum class Role {
    ADMIN,
    AGENT
}