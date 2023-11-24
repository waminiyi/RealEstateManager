package com.waminiyi.realestatemanager.core.data.remote.repository

import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteAgent
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteEstate
import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto

interface RemoteDataRepository {
    suspend fun uploadPhoto(photo: RemotePhoto): DataResult<Unit>
    suspend fun deletePhoto(photoId: String): DataResult<Unit>
    suspend fun getPhoto(photoId: String): DataResult<RemotePhoto?>
    suspend fun getAllEstatePhotos(estateId: String): DataResult<List<RemotePhoto>>

    suspend fun uploadAgent(agent: RemoteAgent): DataResult<Unit>
    suspend fun getAgent(agentId: String): DataResult<RemoteAgent?>
    suspend fun getAllAgents(): DataResult<List<RemoteAgent>>

    suspend fun uploadEstate(estate: RemoteEstate): DataResult<Unit>
    suspend fun getEstate(estateId: String): DataResult<RemoteEstate?>
    suspend fun getAllEstates(): DataResult<List<RemoteEstate>>
}