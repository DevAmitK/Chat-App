package com.example.mychatapp.presentation.FCM


import android.util.Log
import com.example.mychatapp.BuildConfig
import com.example.mychatapp.MainActivity
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.local.repo.LocalRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.domain.usecase.SENDER_USER_ID
import com.example.mychatapp.helper.fcm.Base64Util
import com.example.mychatapp.helper.fcm.NewMessageNotification
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.streamliners.base.exception.defaultExecuteHandlingError
import com.streamliners.helpers.NotificationHelper
import org.koin.android.ext.android.inject

class ChatAppFCMService : FirebaseMessagingService() {

    private val userRepo: UserRepo by inject()
    private val localRepo: LocalRepo by inject()


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
       val data = message.data



        defaultExecuteHandlingError(
            lambda = {
                val objectStr = data["object"] ?: error("New Message Objet Not Received")
                val notification = Base64Util.decodeJson<NewMessageNotification>(objectStr)

                //Skip showing notification if send it self
               if (notification.senderUserId == currentUserId()) return@defaultExecuteHandlingError
                showNotification(notification.title, notification.body)
            },
            buildType = BuildConfig.BUILD_TYPE
        )



    }

//    private fun loadData(data: Map<String, String>) {
//        data.forEach { (key, value) ->
//            Log.i("ChatAppDebug", "Message Receive: ($key , $value)")
//        }
//
//    }

    private fun showNotification(title: String, body: String) {
        NotificationHelper(this)
            .showNotification(
                title = title,
                body = body,
                pendingIntentActivity = MainActivity::class.java
            )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        //TODO User Save In Local & Modify this function

        defaultExecuteHandlingError(
            lambda = {
                if (localRepo.getLoginState()) {
                        userRepo.updateFcmToken(token, currentUserId())
                    }
            },
            buildType = BuildConfig.BUILD_TYPE
        )


    }

}