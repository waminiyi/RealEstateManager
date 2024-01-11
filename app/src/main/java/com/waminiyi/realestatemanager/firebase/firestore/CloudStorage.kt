package com.waminiyi.realestatemanager.firebase.firestore

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

const val PHOTOS="photos"
class CloudStorage @Inject constructor(private val firebaseStorage: FirebaseStorage) {

    suspend fun handleImage(uri: Uri,imageUuid:String ) {
        uploadImage(
            uri,
            imageUuid
        ).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri1: Uri ->
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri1)
                    .build()
            }.addOnFailureListener { e: Exception ->
                Log.d(
                    "TAG-PHOTO-UPLOAD",
                    "failed$e"
                )
            }
        }
    }

    fun uploadImage(imageUri: Uri?, imageUuid:String): UploadTask {
        val mImageRef = firebaseStorage.getReference("$PHOTOS/$imageUuid")
        return mImageRef.putFile(imageUri!!)
    }

   suspend fun deleteImageFromStorage(imagePath: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // Get reference to the image in Firebase Storage
        val storageRef = firebaseStorage.reference.child(imagePath)

        // Delete the image
        storageRef.delete()
            .addOnSuccessListener {
                // Image deleted successfully
                onSuccess.invoke()
            }
            .addOnFailureListener { exception ->
                // An error occurred while deleting the image
                onFailure.invoke(exception)
            }
    }
}