package com.example.mychatapp.helper.fcm





class FcmPayload(
    val message: FcmMessage
)
data class FcmMessage(
    val topic: String?,
    val token: String?,
    val notification: NotificationPayload?,
    val data: Map<String, Any>?,
    val android: AndroidPayload?

){
    companion object {
        fun forTopic(
            topic: String?,
            notification: NotificationPayload? = null,
            data: Map<String, Any>? = null,
            android: AndroidPayload? = null
        ): FcmMessage {
            return FcmMessage(
                topic = topic,
                token = null,
                notification, data, android
            )
        }


        fun forToken(
            token: String?,
            notification: NotificationPayload? = null,
            data: Map<String, Any>? = null,
            android: AndroidPayload? = null

        ): FcmMessage {
            return FcmMessage(
                topic = null,
                token = token,
                notification, data, android
            )
        }
    }
}

class AndroidPayload (
    val priority: String
)

data class NotificationPayload(
    val title: String,
    val body: String
)