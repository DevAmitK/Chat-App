package com.example.mychatapp.presentation.FCM


import android.util.Log
import com.example.mychatapp.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.streamliners.helpers.NotificationHelper

class ChatAppFCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notificationParams = message.notification ?: return
        val title = notificationParams.title ?: return
        val body = notificationParams.body ?: return
        showNotification(title, body)

        val data = message.data
        loadData(data)

    }

    private fun loadData(data: Map<String, String>) {
        data.forEach { (key, value) ->
            Log.i("ChatAppDebug", "Message Receive: ($key , $value)")
        }

    }

    private fun showNotification(title: String, body: String) {
        NotificationHelper(this)
            .showNotification(
                title = title,
                body = body,
                pendingIntentActivity = MainActivity::class.java
            )
    }

}