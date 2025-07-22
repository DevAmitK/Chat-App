package com.example.mychatapp.domain.model

import com.google.firebase.Timestamp


data class Message(
    val time: Timestamp = Timestamp.now(),
    // UserId of the sender
    val sender: String,
    val message: String,
    val mediaUrl: String?,
) {

    constructor() : this(Timestamp.now(), "", "", null)
}