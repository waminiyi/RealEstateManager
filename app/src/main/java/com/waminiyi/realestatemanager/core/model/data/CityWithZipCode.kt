package com.waminiyi.realestatemanager.core.model.data

data class CityWithZipCode(val zip: String, val city: String, val state: String) {
    override fun toString(): String {
        return "$city ($zip), $state"
    }
}
