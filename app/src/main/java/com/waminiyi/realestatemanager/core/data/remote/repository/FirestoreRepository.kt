package com.waminiyi.realestatemanager.core.data.remote.repository

import com.waminiyi.realestatemanager.core.data.remote.model.RemoteAgent
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteChange
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteEstate
import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.firebase.firestore.FirestoreDao
import com.waminiyi.realestatemanager.firebase.model.FirebaseResult
import com.waminiyi.realestatemanager.firebase.model.FirestorePath.CollectionPath
import com.waminiyi.realestatemanager.firebase.model.FirestorePath.DocumentPath
import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val firestoreDao: FirestoreDao) : RemoteDataRepository {

    override suspend fun uploadPhoto(photo: RemotePhoto) {
        uploadData(getPhotoPath(photo.uuid), photo)
    }

    override suspend fun deletePhoto(photoId: String) {
        deleteData(getPhotoPath(photoId))
    }

    override suspend fun getPhoto(photoId: String): RemotePhoto? {
        return getDataInDocument(getPhotoPath(photoId))
    }

    override suspend fun getAllEstatePhotos(estateId: String): List<RemotePhoto> {
        return getAllDataInCollectionWhereEqual(photosCollection, "estateUuid" to estateId)
    }

    override suspend fun uploadAgent(agent: RemoteAgent) {
        uploadData(getAgentPath(agent.agentUuid), agent)
    }

    override suspend fun getAgent(agentId: String): RemoteAgent? {
        return getDataInDocument(getAgentPath(agentId))
    }

    override suspend fun getAllAgents(): List<RemoteAgent> {
        return getAllDataInCollection(agentsCollection)
    }

    override suspend fun uploadEstate(estate: RemoteEstate) {
        uploadData(getEstatePath(estate.estateUuid), estate)
    }

    override suspend fun getEstate(estateId: String): RemoteEstate? {
        return getDataInDocument(getAgentPath(estateId))
    }

    override suspend fun getAllEstates(): List<RemoteEstate> {
        return getAllDataInCollection(estatesCollection)
    }

    override suspend fun getEstatesChangeList(after: Long): List<RemoteChange> {
        TODO("Not yet implemented")
    }

    override suspend fun getPhotosChangeList(after: Long): List<RemoteChange> {
        TODO("Not yet implemented")
    }

    override suspend fun getAgentsChangeList(after: Long): List<RemoteChange> {
        return when (val result = firestoreDao.getListDataInDocument<RemoteChange>(changesDoc)) {
            is FirebaseResult.Success -> result.data
            else -> emptyList()
        }
    }

    override suspend fun updateRemoteChanges(update: (List<LocalChangeEntity>) -> List<RemoteChange>) {
        TODO("Not yet implemented")
    }

    private fun getPhotoPath(id: String): DocumentPath = DocumentPath(listOf("photos", id))
    private fun getAgentPath(id: String): DocumentPath = DocumentPath(listOf("agents", id))
    private fun getEstatePath(id: String): DocumentPath = DocumentPath(listOf("estates", id))
    private val agentsCollection = CollectionPath(listOf("agents"))
    private val estatesCollection = CollectionPath(listOf("estates"))
    private val photosCollection = CollectionPath(listOf("photos"))
    private val changesDoc = DocumentPath(listOf("versions", "versionDoc"))

    private suspend fun <T : Any> uploadData(
        path: DocumentPath,
        data: T
    ): DataResult<Unit> {
        return when (val result = firestoreDao.insertData(path, data)) {
            is FirebaseResult.Success -> DataResult.Success(Unit)
            is FirebaseResult.Error -> DataResult.Error(result.exception)
        }
    }

    private suspend inline fun <reified T : Any> getDataInDocument(path: DocumentPath): T? {
        return when (val result = firestoreDao.getDataInDocument<T>(path)) {
            is FirebaseResult.Success -> result.data
            is FirebaseResult.Error -> null
        }
    }

    private suspend fun deleteData(path: DocumentPath): DataResult<Unit> {
        return when (val result = firestoreDao.deleteDocument(path)) {
            is FirebaseResult.Success -> DataResult.Success(Unit)
            is FirebaseResult.Error -> DataResult.Error(result.exception)
        }
    }

    private suspend inline fun <reified T : Any> getAllDataInCollection(path: CollectionPath): List<T> {
        return when (val result = firestoreDao.getAllDataInCollection<T>(path)) {
            is FirebaseResult.Success -> result.data
            is FirebaseResult.Error -> emptyList()
        }
    }

    private suspend inline fun <reified T : Any> getAllDataInCollectionWhereEqual(
        path: CollectionPath,
        constraint: Pair<String, Any>
    ): List<T> {
        return when (val result = firestoreDao.getAllDataInCollectionWhereEqualTo<T>(path, constraint)) {
            is FirebaseResult.Success -> result.data
            is FirebaseResult.Error -> emptyList()
        }
    }

}