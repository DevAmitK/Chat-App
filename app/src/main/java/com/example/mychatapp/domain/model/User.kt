package com.example.mychatapp.domain.model

import com.google.firebase.Timestamp


data class User(
    val id: String,
    val name: String,
    val email: String,
    val bio: String?,
    val gender: Gender?,
    val imageUri: String?,
    val fcmToken : String ?,
    val lastOnlineTS : Timestamp?

) {
    // Secondary constructor with specific default values
    constructor() : this(
        id = "",
        name = "",
        email = "",
        bio = "",
        gender = null,
        imageUri = null,
        fcmToken = null,
        lastOnlineTS = null
    )
    enum class Gender {
        MALE,
        FEMALE,
        OTHER
    }
}
