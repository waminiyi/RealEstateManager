package com.waminiyi.realestatemanager.firebase.firestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.waminiyi.realestatemanager.firebase.model.FirebaseResult
import com.waminiyi.realestatemanager.firebase.model.FirestorePath.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirestoreDao @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun <T : Any> insertData(
        path: DocumentPath,
        data: T
    ): FirebaseResult<Unit> {
        return handleTask(createDocReference(path).set(data), "Data upload failed in $path")
    }

    suspend fun deleteDocument(
        path: DocumentPath,
    ): FirebaseResult<Unit> {
        return handleTask(createDocReference(path).delete(), "Failed deleting the document in $path")
    }


    suspend fun <T : Any> bulkInsertData(
        dataListWithPath: List<Pair<T, DocumentPath>>
    ): FirebaseResult<Unit> {
        val batch = firestore.batch()
        dataListWithPath.forEach { (data, path) ->
            val documentRef = createDocReference(path)
            batch.set(documentRef, data)
        }

        return try {
            handleTask(batch.commit(), " Batch commit failed")
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend fun <T : Any> bulkInsertDataInCollection(
        collectionPath: CollectionPath,
        dataListWithPath: List<Pair<T, String>>
    ): FirebaseResult<Unit> {
        val batch = firestore.batch()
        dataListWithPath.forEach { (data, docId) ->
            val documentPath = DocumentPath(collectionPath.pathSegments + docId)
            val documentRef = createDocReference(documentPath)
            batch.set(documentRef, data)
        }

        return try {
            handleTask(batch.commit(), "Batch commit failed for $collectionPath")
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    /**
     * update some fields of a document without overwriting the entire document
     * @param path: document path
     * @param updates: Hashmap where the key is a string representing the field to update
     * the value can be of any type. The key support  "dot notation"  for nested fields
     */
    suspend fun updateData(
        path: DocumentPath,
        updates: HashMap<String, Any>
    ): FirebaseResult<Unit> {
        return handleTask(createDocReference(path).update(updates), "Update failed for $path")
    }

    /**
     * Retrieves data from a Firestore document.
     *
     * @param path The Firestore document path.
     * @param type The class type of the data being retrieved.
     * @return A [FirebaseResult] indicating the result of the operation along with the retrieved data.
     */
    suspend inline fun <reified T : Any> getDataInDocument(path: DocumentPath): FirebaseResult<T> {
        return when (val result = handleDocumentSnapshotTask(createDocReference(path).get())) {
            is FirebaseResult.Success -> {
                result.data.toObject(T::class.java)?.let {
                    FirebaseResult.Success(it)
                } ?: FirebaseResult.Error(Exception("Failed to parse document data"))
            }

            is FirebaseResult.Error -> FirebaseResult.Error(result.exception)
        }
    }

    /**
     * Retrieves data from a Firestore collection.
     *
     * @param path The Firestore collection path.
     * @param type The class type of the data being retrieved.
     * @return A [FirebaseResult] indicating the result of the operation along with the retrieved data.
     */
    suspend inline fun <reified T : Any> getAllDataInCollection(path: CollectionPath): FirebaseResult<List<T>> {
        val result = handleQuerySnapshotTask(createCollectionReference(path).get())
        return try {
            when (result) {
                is FirebaseResult.Success -> {
                    val dataList = result.data.documents.mapNotNull {
                        it.toObject(T::class.java)
                    }
                    FirebaseResult.Success(dataList)
                }

                is FirebaseResult.Error -> FirebaseResult.Error(result.exception)
            }
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    suspend inline fun <reified T : Any> getAllDataInCollectionWhereEqualTo(
        path: CollectionPath,
        constraint: Pair<String, Any>
    ): FirebaseResult<List<T>> {
        val result = handleQuerySnapshotTask(
            createCollectionReference(path)
                .whereEqualTo(constraint.first, constraint.second)
                .get()
        )
        return try {
            when (result) {
                is FirebaseResult.Success -> {
                    val dataList = result.data.documents.mapNotNull {
                        it.toObject(T::class.java)
                    }
                    FirebaseResult.Success(dataList)
                }

                is FirebaseResult.Error -> FirebaseResult.Error(result.exception)
            }
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }

    private suspend fun handleTask(task: Task<Void>, failureMessage: String): FirebaseResult<Unit> {
        return suspendCoroutine { continuation ->
            task.addOnCompleteListener { savingTask ->
                if (savingTask.isSuccessful) {
                    continuation.resume(FirebaseResult.Success(Unit))
                } else {
                    val exception = savingTask.exception
                    val errorMessage = exception?.message ?: failureMessage
                    continuation.resume(FirebaseResult.Error(Exception(errorMessage)))
                }
            }
        }
    }

    suspend fun handleDocumentSnapshotTask(task: Task<DocumentSnapshot>): FirebaseResult<DocumentSnapshot> {
        return suspendCoroutine { continuation ->
            task.addOnCompleteListener { retrievingTask ->
                if (retrievingTask.isSuccessful) {
                    if (retrievingTask.result.exists()) {
                        continuation.resume(FirebaseResult.Success(retrievingTask.result))
                    } else {
                        continuation.resume(FirebaseResult.Error(Exception("Document not found")))
                    }
                } else {
                    val exception = retrievingTask.exception
                    val errorMessage = exception?.message ?: "Data retrieving failed"
                    continuation.resume(FirebaseResult.Error(Exception(errorMessage)))
                }
            }
        }
    }

    suspend fun handleQuerySnapshotTask(task: Task<QuerySnapshot>): FirebaseResult<QuerySnapshot> {
        return suspendCoroutine { continuation ->
            task.addOnCompleteListener { retrievingTask ->
                if (retrievingTask.isSuccessful) {
                    continuation.resume(FirebaseResult.Success(retrievingTask.result))
                } else {
                    val exception = retrievingTask.exception
                    val errorMessage = exception?.message ?: "Data retrieving failed"
                    continuation.resume(FirebaseResult.Error(Exception(errorMessage)))
                }
            }
        }
    }

    fun createDocReference(path: DocumentPath): DocumentReference {
        return if (path.isDocIdAutoGenerated()) {
            firestore.collection(path.toString()).document()
        } else {
            firestore.document(path.toString())
        }
    }

    fun createCollectionReference(path: CollectionPath): CollectionReference {
        return firestore.collection(path.toString())
    }
}