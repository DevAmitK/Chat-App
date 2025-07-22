package com.example.mychatapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore

object FireBaseCollection {
    fun FirebaseFirestore.userCollection () =  collection("users")
    fun  FirebaseFirestore.userChannel () = collection("channel")
    fun  FirebaseFirestore.otherCollection () = collection("other")

}


const val USER_IMAGE = "UserImage"