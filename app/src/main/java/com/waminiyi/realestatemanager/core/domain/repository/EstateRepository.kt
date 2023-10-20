package com.waminiyi.realestatemanager.core.domain.repository

import com.waminiyi.realestatemanager.core.domain.model.estate.Estate

interface EstateRepository {
    suspend fun addEstate(estate: Estate)
}