package com.example.mychatapp.data.remote

import com.example.mychatapp.data.remote.FireBaseCollection.userChannel
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.ext.imageUri
import com.example.mychatapp.domain.ext.otherUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.Message
import com.example.mychatapp.domain.model.User
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

    override suspend fun createOneToOneChannel(
        currentUserId: String, otherUserId: String
    ): String {

        val culRef = firestore.userChannel()
        val id = culRef.document().id

        culRef.document(id).set(
            Channel(
                imageUrl = null,
                name ="OneToOne",
                type = Channel.Type.OneToOne,
                description = null,
                members = listOf(currentUserId, otherUserId),
                messages = emptyList(),
            )

        ).await()

        return id
    }

    override suspend fun getAllChannels(currentUser: String, users: List<User>): List<Channel> {
        return firestore.userChannel()
            .whereArrayContains(Channel::members.name, currentUser)
            .get()
            .await()
            .toObjects(Channel::class.java)
            .map { transformChannelObjects(channel = it, users = users, userId = currentUser) }

    }

    override suspend fun getChannel(channelId: String): Channel {
        return firestore.userChannel()
            .document(channelId)
            .get()
            .await()
            .toObject(Channel::class.java)?: error("Channel Not Found $channelId")
    }

    override suspend fun getChannelWithFlowMessage(channelId: String, users: List<User>, id: String): Flow<Channel> =
        callbackFlow {

            firestore.userChannel()
                .document(channelId)
                .addSnapshotListener { value, e ->
                    e?.let { throw it }
                    val channel =  value?.toObject(Channel::class.java)
                    if (channel != null) {
                        trySend(transformChannelObjects(channel = channel, users = users, userId = id))
                    }
                }
            awaitClose()
        }

    override suspend fun createGroupChannel(
        currentUserId: String,
        name: String,
        description: String,
        groupImage: String?,
        members: List<String>,
    ): String {

        val culRef = firestore.userChannel()
        val id = culRef.document().id

        culRef.document(id).set(
            Channel(
                imageUrl = groupImage,
                name = name,
                type = Channel.Type.Group,
                description =description,
                members = members+currentUserId,
                messages = emptyList(),
            )

        ).await()

        return id
    }


    override suspend fun sendMessage(channelId: String, message: Message) {
        firestore.userChannel()
            .document(channelId)
            .update(Channel::messages.name , FieldValue.arrayUnion(message))
            .await()
    }

    private fun transformChannelObjects(channel :Channel,users: List<User>,userId : String): Channel {

        return if (channel.type == Channel.Type.OneToOne) {

            val otherUserId = channel.otherUserId(userId)

            val otherUser = users.find {
                it.id() == otherUserId
            } ?: error("User with Id $otherUserId Not Found")

            channel.copy(
                name = otherUser.name,
                imageUrl = otherUser.imageUri()
            )
        } else {
            channel
        }
    }
}