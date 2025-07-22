package com.example.mychatapp.domain.model

import com.google.firebase.firestore.DocumentId

data class Channel(
    @DocumentId
    val id: String?=null,
    val imageUrl: String?=null,
    val type: Type,
    val name: String,
    val description: String?,
    // List of UserId of members
    val members: List<String>,
    val messages: List<Message>
){
    constructor(): this(null, null, Type.OneToOne, "", null, emptyList(), emptyList())

    enum class Type{
        OneToOne, Group
    }
}
