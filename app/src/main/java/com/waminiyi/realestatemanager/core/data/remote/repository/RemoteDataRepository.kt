package com.waminiyi.realestatemanager.core.data.remote.repository

import com.waminiyi.realestatemanager.core.data.remote.model.RemoteAgent
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteChange
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteCommit
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteEstate
import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import java.sql.Timestamp

interface RemoteDataRepository {
    suspend fun uploadPhoto(photo: RemotePhoto)
    suspend fun deletePhoto(photoId: String)
    suspend fun getPhoto(photoId: String): RemotePhoto?
    suspend fun getAllEstatePhotos(estateId: String): List<RemotePhoto>

    suspend fun uploadAgent(agent: RemoteAgent)
    suspend fun getAgent(agentId: String): RemoteAgent?
    suspend fun getAllAgents(): List<RemoteAgent>

    suspend fun uploadEstate(estate: RemoteEstate)
    suspend fun getEstate(estateId: String):RemoteEstate?
    suspend fun getAllEstates():List<RemoteEstate>

    suspend fun getEstatesChangeList(after: Long): List<RemoteChange>
    suspend fun getPhotosChangeList(after: Long): List<RemoteChange>
    suspend fun getAgentsChangeList(after: Long): List<RemoteChange>

    suspend fun updateRemoteChanges(update: (List<LocalChangeEntity>) -> List<RemoteChange>)

}