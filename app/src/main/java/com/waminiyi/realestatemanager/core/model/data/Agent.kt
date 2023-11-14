package com.waminiyi.realestatemanager.core.model.data

import com.waminiyi.realestatemanager.core.model.data.Image

data class Agent(
    val uuid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val image: Image
)
