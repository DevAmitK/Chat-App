package com.example.mychatapp.data.remote

import android.net.Uri
import com.example.mychatapp.domain.remote.StorageRepo
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class StorageRepoImpl : StorageRepo {
    override suspend fun uploadFile(path: String, uri: Uri): String {
        return try {
            // Get the reference to the file location in Firebase Storage
            val storageReference = Firebase.storage.getReference(path)

            // Upload the file
            storageReference.putFile(uri).await()

            // Get the download URL after the upload is complete
            val downloadUrl = storageReference.downloadUrl.await()

            // Return the download URL as a string
            downloadUrl.toString()

        } catch (exception: Exception) {
            throw IllegalStateException("Failed to upload file or retrieve URL", exception)
        }
    }
}