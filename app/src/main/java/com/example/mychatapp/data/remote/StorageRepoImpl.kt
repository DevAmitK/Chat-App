package com.example.mychatapp.data.remote

import androidx.core.net.toUri
import com.example.mychatapp.domain.remote.StorageRepo
import com.example.mychatapp.ui.comp.ImageState
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class StorageRepoImpl : StorageRepo {
    override suspend fun uploadFile(path: String, imageState: ImageState): String? {

       return when(imageState){
            ImageState.Empty -> null
            is ImageState.Exists -> imageState.url
            is ImageState.New -> {
                // Get the reference to the file location in Firebase Storage
                val storageReference = Firebase.storage.getReference(path)
                // Upload the file
                storageReference.putFile(imageState.pickedMedia.uri.toUri()).await()
                // Get the download URL after the upload is complete
                val downloadUrl = storageReference.downloadUrl.await()
                // Return the download URL as a string
                downloadUrl.toString()
            }
        }





    }
}