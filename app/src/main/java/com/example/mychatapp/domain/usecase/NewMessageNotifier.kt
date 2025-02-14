package com.example.mychatapp.domain.usecase

import android.adservices.topics.Topic
import com.example.mychatapp.domain.remote.OtherRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.helper.fcm.AndroidPayload
import com.example.mychatapp.helper.fcm.FcmMessage
import com.example.mychatapp.helper.fcm.FcmPayload
import com.example.mychatapp.helper.fcm.FcmSender
import com.example.mychatapp.helper.fcm.NotificationPayload

class NewMessageNotifier(
    private val otherRepo: OtherRepo,
    private val fcmSender: FcmSender,
    private val userRepo: UserRepo,
){
    suspend fun notifySingleUser(
        userId: String,
        userName: String,
        message: String,
    ) {

        val token = userRepo.getUserById(id = userId).fcmToken ?: return
      val payload = FcmPayload(
          FcmMessage.forToken(
              token = token,
                notification = NotificationPayload(
                    title = userName,
                    body = message
                ),
                android = AndroidPayload(
                    priority = "high"
                )
            )
        )
        sendNotification(payload)

}

    suspend fun notifyMultipleUsers(
        userName: String,
        message: String,
        topic: String
    ) {
        val payload = FcmPayload(
            FcmMessage.forTopic(
                topic = topic,
                notification = NotificationPayload(
                    title = userName,
                    body = message
                ),
                android = AndroidPayload(
                    priority = "high"
                )
            )
        )
        sendNotification(payload)

    }





    private suspend fun sendNotification(payload: FcmPayload) {
        val scvAcJson = otherRepo.getServiceAccountJson()
        fcmSender.send(fcmPayload = payload , serviceAccountJson = scvAcJson)
    }

}