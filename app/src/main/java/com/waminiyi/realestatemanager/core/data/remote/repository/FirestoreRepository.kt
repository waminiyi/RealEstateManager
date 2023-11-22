package com.waminiyi.realestatemanager.core.data.remote.repository

import com.waminiyi.realestatemanager.core.model.data.Result
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteAgent
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteEstate
import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto
import com.waminiyi.realestatemanager.firebase.firestore.FirestoreDao
import com.waminiyi.realestatemanager.firebase.model.FirebaseResult
import com.waminiyi.realestatemanager.firebase.model.FirestorePath.CollectionPath
import com.waminiyi.realestatemanager.firebase.model.FirestorePath.DocumentPath
import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val firestoreDao: FirestoreDao) : RemoteDataRepository {

    override suspend fun uploadPhoto(photo: RemotePhoto): Result<Unit> {
        return uploadData(getPhotoPath(photo.uuid), photo)
    }

    override suspend fun deletePhoto(photoId: String): Result<Unit> {
        return deleteData(getPhotoPath(photoId))
    }

    override suspend fun getPhoto(photoId: String): Result<RemotePhoto?> {
        return getDataInDocument(getPhotoPath(photoId))
    }

    override suspend fun getAllEstatePhotos(estateId: String): Result<List<RemotePhoto>> {
        return getAllDataInCollectionWhereEqual(photosCollection, "estateUuid" to estateId)
    }

    override suspend fun uploadAgent(agent: RemoteAgent): Result<Unit> {
        return uploadData(getAgentPath(agent.agentUuid), agent)
    }

    override suspend fun getAgent(agentId: String): Result<RemoteAgent?> {
        return getDataInDocument(getAgentPath(agentId))
    }

    override suspend fun getAllAgents(): Result<List<RemoteAgent>> {
        return getAllDataInCollection(agentsCollection)
    }

    override suspend fun uploadEstate(estate: RemoteEstate): Result<Unit> {
        return uploadData(getEstatePath(estate.estateUuid), estate)
    }

    override suspend fun getEstate(estateId: String): Result<RemoteEstate?> {
        return getDataInDocument(getAgentPath(estateId))
    }

    override suspend fun getAllEstates(): Result<List<RemoteEstate>> {
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
    ): Result<Unit> {
        return when (val result = firestoreDao.insertData(path, data)) {
            is FirebaseResult.Success -> Result.Success(Unit)
            is FirebaseResult.Error -> Result.Error(result.exception)
        }
    }

    private suspend inline fun <reified T : Any> getDataInDocument(path: DocumentPath): Result<T> {
        return when (val result = firestoreDao.getDataInDocument<T>(path)) {
            is FirebaseResult.Success -> Result.Success(result.data)
            is FirebaseResult.Error -> Result.Error(result.exception)
        }
    }

    private suspend fun deleteData(path: DocumentPath): Result<Unit> {
        return when (val result = firestoreDao.deleteDocument(path)) {
            is FirebaseResult.Success -> Result.Success(Unit)
            is FirebaseResult.Error -> Result.Error(result.exception)
        }
    }

    private suspend inline fun <reified T : Any> getAllDataInCollection(path: CollectionPath): Result<List<T>> {
        return when (val result = firestoreDao.getAllDataInCollection<T>(path)) {
            is FirebaseResult.Success -> Result.Success(result.data)
            is FirebaseResult.Error -> Result.Error(result.exception)
        }
    }

    private suspend inline fun <reified T : Any> getAllDataInCollectionWhereEqual(
        path: CollectionPath,
        constraint: Pair<String, Any>
    ): Result<List<T>> {
        return when (val result = firestoreDao.getAllDataInCollectionWhereEqualTo<T>(path, constraint)) {
            is FirebaseResult.Success -> Result.Success(result.data)
            is FirebaseResult.Error -> Result.Error(result.exception)
        }
    }

}