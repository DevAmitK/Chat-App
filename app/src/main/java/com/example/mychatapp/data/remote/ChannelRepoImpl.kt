package com.example.mychatapp.data.remote

import com.example.mychatapp.data.remote.FireBaseCollection.userChannel
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.Message
import com.example.mychatapp.domain.remote.ChannelRepo
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChannelRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChannelRepo {

    override suspend fun getOneToOneChat(
        currentUserId: String,
        otherUserId: String,
    ): Channel? {
        return firestore.userChannel()
            .whereEqualTo(Channel::type.name, Channel.Type.OneToOne)
            .whereArrayContainsAny(Channel::members.name, listOf(currentUserId, otherUserId))
            .get()
            .await()
            .toObjects(Channel::class.java)
            .firstOrNull {
                it.members == listOf(currentUserId, otherUserId) ||
                        it.members == listOf( otherUserId,currentUserId)
            }
    }

    override suspend fun createOneToOneChannel(currentUserId: String, otherUserId: String): String {

        val culRef = firestore.userChannel()
        val id = culRef.document().id

        culRef.document(id).set(
            Channel(
                imageUrl = null,
                name = "Amit",
                type = Channel.Type.OneToOne,
                description = null,
                members = listOf(currentUserId, otherUserId),
                messages = emptyList(),
            )

        ).await()

        return id
    }

    override suspend fun getAllChannels(currentUser: String): List<Channel> {
        return firestore.userChannel()
            .whereEqualTo(Channel::type.name, Channel.Type.OneToOne)
            .whereArrayContains(Channel::members.name, currentUser)
            .get()
            .await()
            .toObjects(Channel::class.java)
    }

    override suspend fun getChannel(channelId: String): Channel {
        return firestore.userChannel()
            .document(channelId)
            .get()
            .await()
            .toObject(Channel::class.java)?: error("Channel Not Found $channelId")
    }

    override suspend fun getChannelWithFlowMessage(channelId: String): Flow<Channel> =
        callbackFlow {

            firestore.userChannel()
                .document(channelId)
                .addSnapshotListener { value, e ->
                    e?.let { throw it }
                    val channel =  value?.toObject(Channel::class.java)
                    if (channel != null) {
                        trySend(channel)
                    }
                }
            awaitClose()
        }

    override suspend fun sendMassage(channelId: String, message: Message) {
        firestore.userChannel()
            .document(channelId)
            .update(Channel::messages.name , FieldValue.arrayUnion(message))
            .await()
    }
}