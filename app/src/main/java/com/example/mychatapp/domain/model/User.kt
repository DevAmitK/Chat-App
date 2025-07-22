package com.example.mychatapp.domain.model


data class User(
    val id: String,
    val name: String,
    val email: String,
    val bio: String?,
    val gender: Gender?,
    val imageUri: String? = null
) {
    // Secondary constructor with specific default values
    constructor() : this(
        id = "",
        name = "",
        email = "",
        bio = "",
        gender = null,
        imageUri = null
    )
    enum class Gender {
        MALE,
        FEMALE,
        OTHER
    }
}
