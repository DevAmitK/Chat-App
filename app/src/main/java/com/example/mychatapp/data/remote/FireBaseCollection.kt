package com.example.mychatapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore

object FireBaseCollection {
    fun FirebaseFirestore.userCollection () =  collection("users")
    fun  FirebaseFirestore.userChannel () = collection("channel")

}


const val USER_IMAGE = "UserImage"