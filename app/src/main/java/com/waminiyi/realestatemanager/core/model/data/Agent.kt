package com.waminiyi.realestatemanager.core.model.data

data class Agent(
    val uuid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val photoUrl: String,
    val roles: Set<Role> = setOf(Role.AGENT)
)

enum class Role {
    ADMIN,
    AGENT
}