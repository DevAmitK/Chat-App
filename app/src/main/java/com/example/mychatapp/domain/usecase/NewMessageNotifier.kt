package com.example.mychatapp.domain.usecase

import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.OtherRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.helper.fcm.Base64Util
import com.example.mychatapp.helper.fcm.FcmMessage
import com.example.mychatapp.helper.fcm.FcmPayload
import com.example.mychatapp.helper.fcm.FcmSender
import com.example.mychatapp.helper.fcm.Notification
import com.example.mychatapp.helper.fcm.Notification.Companion.supportingGson


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


        val newMessageNotification = Notification.NewMessageNotification(
            title = userName,
            body = message
        )

        val payload = FcmPayload(
            FcmMessage.forToken(
                token = token,
                data = mapOf(
                    "object" to Base64Util.encodeAsJson(newMessageNotification,gson = supportingGson())
                ),
            )
        )
            sendNotification(payload)


    }

    suspend fun notifyMultipleUsers(
        sender: User,
        message: String,
        topic: String
    ) {
        val newMessageNotification =Notification.NewMessageNotification(
            title = sender.name,
            body = message,
            senderUserId = sender.id()
        )
        val payload = FcmPayload(
            FcmMessage.forTopic(
                topic = topic,
                data = mapOf(
                    "object" to Base64Util.encodeAsJson(any = newMessageNotification, gson = supportingGson())
                ),
            )
        )
        sendNotification(payload)

    }





    private suspend fun sendNotification(payload: FcmPayload) {
        val scvAcJson = otherRepo.getServiceAccountJson()
        fcmSender.send(fcmPayload = payload , serviceAccountJson = scvAcJson)
    }

}
