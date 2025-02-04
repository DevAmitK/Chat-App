package com.example.mychatapp.helper.fcm


import com.google.auth.oauth2.GoogleCredentials
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.io.ByteArrayInputStream

class FcmSender(
    private val client: HttpClient
) {

    private val projectId = "e-book-d6a38"
    private val endpoint = "https://fcm.googleapis.com/v1/projects/$projectId/messages:send"

    suspend fun send(fcmPayload: FcmPayload, serviceAccountJson: String){
        val token = getAccessToken(serviceAccountJson)

        client.post(endpoint) {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(
                Gson().toJson(fcmPayload)
            )
        }
    }
    private fun getAccessToken(serviceAccountJson: String): String? {
        val googleCredentials = GoogleCredentials
            .fromStream(
                ByteArrayInputStream(serviceAccountJson.toByteArray())
            )
            .createScoped(
                listOf("https://www.googleapis.com/auth/firebase.messaging")
            )
        googleCredentials.refresh()
        return googleCredentials.getAccessToken().tokenValue
    }


}