package com.waminiyi.realestatemanager.core.domain.model.agent

import com.waminiyi.realestatemanager.core.domain.model.estate.Image

data class Agent(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val image: Image
)
