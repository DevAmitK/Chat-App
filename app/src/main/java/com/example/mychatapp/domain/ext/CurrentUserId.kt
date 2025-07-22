package com.example.mychatapp.domain.ext

import com.google.firebase.Firebase
import com.google.firebase.auth.auth


fun currentUserId() : String {
    return  Firebase.auth.currentUser?.uid ?: error("Current User Not Found")
}