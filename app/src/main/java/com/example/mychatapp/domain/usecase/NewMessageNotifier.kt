package com.example.mychatapp.domain.usecase

import android.util.Log
import com.example.mychatapp.domain.remote.OtherRepo
import com.example.mychatapp.helper.fcm.AndroidPayload
import com.example.mychatapp.helper.fcm.FcmMessage
import com.example.mychatapp.helper.fcm.FcmPayload
import com.example.mychatapp.helper.fcm.FcmSender
import com.example.mychatapp.helper.fcm.NotificationPayload

class NewMessageNotifier (
    private val otherRepo: OtherRepo,
    private val fcmSender: FcmSender
){
suspend fun notify(){
    val scvAcJson = otherRepo.getServiceAccountJson()
    Log.d("scvAcPayload", "notify: $scvAcJson")

      val payload = FcmPayload(
            FcmMessage.forTopic(
                topic = "general",
                notification = NotificationPayload(
                    title = "Chat App General Message",
                    body = "Hi I am Amit"
                ),
                android = AndroidPayload(
                    priority = "high"
                )
            )
        )

    fcmSender.send(fcmPayload = payload , serviceAccountJson = scvAcJson)
}

}