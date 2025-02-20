package com.example.mychatapp.domain.usecase

import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.OtherRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.helper.fcm.AndroidPayload
import com.example.mychatapp.helper.fcm.FcmMessage
import com.example.mychatapp.helper.fcm.FcmPayload
import com.example.mychatapp.helper.fcm.FcmSender
import com.example.mychatapp.helper.fcm.NotificationPayload


const val SENDER_USER_ID = "senderUserId"

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
              data = mapOf("title" to userName,
                  "body" to message),
            )
        )
        sendNotification(payload)

}

    suspend fun notifyMultipleUsers(
        sender: User,
        message: String,
        topic: String
    ) {
        val payload = FcmPayload(
            FcmMessage.forTopic(
                topic = topic,
                data = mapOf("title" to sender.name,
                    "body" to message,
                    SENDER_USER_ID to sender.id()),
            )
        )
        sendNotification(payload)

    }





    private suspend fun sendNotification(payload: FcmPayload) {
        val scvAcJson = otherRepo.getServiceAccountJson()
        fcmSender.send(fcmPayload = payload , serviceAccountJson = scvAcJson)
    }

}
