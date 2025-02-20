package com.example.mychatapp.helper.fcm

import com.google.gson.GsonBuilder

sealed class Notification {
    class NewMessageNotification (
        val title : String,
        val body : String,
        val senderUserId : String? = null
    ) : Notification()


    companion object{
        fun supportingGson() = GsonBuilder()
            .registerTypeAdapterFactory(
            SealedTypeAdapterFactory.of(Notification::class)
        ).create()
    }
}