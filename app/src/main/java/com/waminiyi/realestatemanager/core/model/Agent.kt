package com.waminiyi.realestatemanager.core.model

/**
 * Data class representing an agent.
 * @property uuid The unique identifier of the agent.
 * @property firstName The first name of the agent.
 * @property lastName The last name of the agent.
 * @property email The email address of the agent.
 * @property phoneNumber The phone number of the agent.
 * @property photoUrl The URL to the photo of the agent.
 */
data class Agent(
    val uuid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val photoUrl: String,
)
