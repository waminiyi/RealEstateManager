package com.waminiyi.realestatemanager.core.data.remote.repository

import com.waminiyi.realestatemanager.core.model.data.Result
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteAgent
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteEstate
import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto

interface RemoteDataRepository {
    suspend fun uploadPhoto(photo: RemotePhoto): Result<Unit>
    suspend fun deletePhoto(photoId: String): Result<Unit>
    suspend fun getPhoto(photoId: String): Result<RemotePhoto?>
    suspend fun getAllEstatePhotos(estateId: String): Result<List<RemotePhoto>>

    suspend fun uploadAgent(agent: RemoteAgent): Result<Unit>
    suspend fun getAgent(agentId: String): Result<RemoteAgent?>
    suspend fun getAllAgents(): Result<List<RemoteAgent>>

    suspend fun uploadEstate(estate: RemoteEstate): Result<Unit>
    suspend fun getEstate(estateId: String): Result<RemoteEstate?>
    suspend fun getAllEstates(): Result<List<RemoteEstate>>
}