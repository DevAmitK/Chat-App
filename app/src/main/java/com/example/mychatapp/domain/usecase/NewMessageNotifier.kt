package com.example.mychatapp.domain.usecase

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
    suspend fun notify(
        userId: String,
        userName: String,
        message: String,
    ) {
    val scvAcJson = otherRepo.getServiceAccountJson()
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

    fcmSender.send(fcmPayload = payload , serviceAccountJson = scvAcJson)
}

}