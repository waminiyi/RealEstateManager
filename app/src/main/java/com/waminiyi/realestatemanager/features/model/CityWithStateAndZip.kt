package com.waminiyi.realestatemanager.features.model

import kotlinx.serialization.Serializable

@Serializable
data class CityWithStateAndZip(val city: String, val state: String, val zip: String)