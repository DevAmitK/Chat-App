package com.example.mychatapp.domain.remote

import android.net.Uri

interface StorageRepo {
    suspend fun uploadFile(path : String , uri : Uri) : String
}