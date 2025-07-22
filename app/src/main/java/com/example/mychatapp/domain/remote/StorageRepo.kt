package com.example.mychatapp.domain.remote

import com.example.mychatapp.ui.comp.ImageState


interface StorageRepo {
    suspend fun uploadFile(path : String, imageState : ImageState) : String?
}