package com.example.mychatapp.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.example.mychatapp.data.remote.FireBaseCollection.otherCollection
import com.example.mychatapp.domain.ext.Secret
import com.example.mychatapp.domain.remote.OtherRepo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class OtherRepoImpl() : OtherRepo{
    @SuppressLint("SuspiciousIndentation")
    override suspend fun getServiceAccountJson(): String {
      val scvAc =
          Firebase.firestore.otherCollection()
              .document("secret")
              .get()
              .await()
              .toObject(Secret::class.java)
              ?.svcAc
            ?: error("Service Account Not Found Is Firebase ")

        Log.d("scvAcPayload", "getServiceAccountJson:$scvAc ")

        return  scvAc

    }


}