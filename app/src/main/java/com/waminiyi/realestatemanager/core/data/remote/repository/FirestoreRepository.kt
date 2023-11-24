package com.waminiyi.realestatemanager.core.data.remote.repository

import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteAgent
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteEstate
import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto
import com.waminiyi.realestatemanager.firebase.firestore.FirestoreDao
import com.waminiyi.realestatemanager.firebase.model.FirebaseResult
import com.waminiyi.realestatemanager.firebase.model.FirestorePath.CollectionPath
import com.waminiyi.realestatemanager.firebase.model.FirestorePath.DocumentPath
import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val firestoreDao: FirestoreDao) : RemoteDataRepository {

    override suspend fun uploadPhoto(photo: RemotePhoto): DataResult<Unit> {
        return uploadData(getPhotoPath(photo.uuid), photo)
    }

    override suspend fun deletePhoto(photoId: String): DataResult<Unit> {
        return deleteData(getPhotoPath(photoId))
    }

    override suspend fun getPhoto(photoId: String): DataResult<RemotePhoto?> {
        return getDataInDocument(getPhotoPath(photoId))
    }

    override suspend fun getAllEstatePhotos(estateId: String): DataResult<List<RemotePhoto>> {
        return getAllDataInCollectionWhereEqual(photosCollection, "estateUuid" to estateId)
    }

    override suspend fun uploadAgent(agent: RemoteAgent): DataResult<Unit> {
        return uploadData(getAgentPath(agent.agentUuid), agent)
    }

    override suspend fun getAgent(agentId: String): DataResult<RemoteAgent?> {
        return getDataInDocument(getAgentPath(agentId))
    }

    override suspend fun getAllAgents(): DataResult<List<RemoteAgent>> {
        return getAllDataInCollection(agentsCollection)
    }

    override suspend fun uploadEstate(estate: RemoteEstate): DataResult<Unit> {
        return uploadData(getEstatePath(estate.estateUuid), estate)
    }

    override suspend fun getEstate(estateId: String): DataResult<RemoteEstate?> {
        return getDataInDocument(getAgentPath(estateId))
    }

    override suspend fun getAllEstates(): DataResult<List<RemoteEstate>> {
        return getAllDataInCollection(estatesCollection)
    }

    private fun getPhotoPath(id: String): DocumentPath = DocumentPath(listOf("photos", id))
    private fun getAgentPath(id: String): DocumentPath = DocumentPath(listOf("agents", id))
    private fun getEstatePath(id: String): DocumentPath = DocumentPath(listOf("estates", id))
    private val agentsCollection = CollectionPath(listOf("agents"))
    private val estatesCollection = CollectionPath(listOf("estates"))
    private val photosCollection = CollectionPath(listOf("photos"))

    private suspend fun <T : Any> uploadData(
        path: DocumentPath,
        data: T
    ): DataResult<Unit> {
        return when (val result = firestoreDao.insertData(path, data)) {
            is FirebaseResult.Success -> DataResult.Success(Unit)
            is FirebaseResult.Error -> DataResult.Error(result.exception)
        }
    }

    private suspend inline fun <reified T : Any> getDataInDocument(path: DocumentPath): DataResult<T> {
        return when (val result = firestoreDao.getDataInDocument<T>(path)) {
            is FirebaseResult.Success -> DataResult.Success(result.data)
            is FirebaseResult.Error -> DataResult.Error(result.exception)
        }
    }

    private suspend fun deleteData(path: DocumentPath): DataResult<Unit> {
        return when (val result = firestoreDao.deleteDocument(path)) {
            is FirebaseResult.Success -> DataResult.Success(Unit)
            is FirebaseResult.Error -> DataResult.Error(result.exception)
        }
    }

    private suspend inline fun <reified T : Any> getAllDataInCollection(path: CollectionPath): DataResult<List<T>> {
        return when (val result = firestoreDao.getAllDataInCollection<T>(path)) {
            is FirebaseResult.Success -> DataResult.Success(result.data)
            is FirebaseResult.Error -> DataResult.Error(result.exception)
        }
    }

    private suspend inline fun <reified T : Any> getAllDataInCollectionWhereEqual(
        path: CollectionPath,
        constraint: Pair<String, Any>
    ): DataResult<List<T>> {
        return when (val result = firestoreDao.getAllDataInCollectionWhereEqualTo<T>(path, constraint)) {
            is FirebaseResult.Success -> DataResult.Success(result.data)
            is FirebaseResult.Error -> DataResult.Error(result.exception)
        }
    }

}