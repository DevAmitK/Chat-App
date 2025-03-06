package com.example.mychatapp.presentation.homeScreen

import androidx.compose.runtime.mutableStateMapOf
import com.example.mychatapp.domain.Constants.OnlineTSUpdater
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.ext.imageUri
import com.example.mychatapp.domain.ext.otherUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import kotlinx.coroutines.tasks.await
import java.lang.System.currentTimeMillis
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    private val channelRepo: ChannelRepo,
    private val userRepo: UserRepo
):BaseViewModel() {

    val channelsState = taskStateOf<List<Channel>>()
    val userOnlineStatus = mutableStateMapOf<String, Boolean>()

    fun start() {
        execute(showLoadingDialog = false) {
            val users = userRepo.getAllUser()
            val channels = channelRepo.getAllChannels(currentUser = currentUserId())
                .map { channel ->
                    if (channel.type == Channel.Type.OneToOne) {

                        val otherUserId = channel.otherUserId(currentUserId())

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
            channelsState.update(channels)
            checkOnlineStatusOfUsers(users)
            subscribeForGroupNotification()
        }
    }

    private fun checkOnlineStatusOfUsers(users: List<User>) {
        users.forEach { user ->
            userOnlineStatus[user.id()] = user.lastOnlineTS.isOnline()
        }
    }

    fun isChannelOneToOneAndOnline(channel: Channel): Boolean {
       return if (channel.type == Channel.Type.OneToOne) {
           val otherUserId= channel.otherUserId(currentUserId())
            userOnlineStatus[otherUserId] ?: false
        }else false
    }

    private fun Timestamp?.isOnline(): Boolean {
        return this?.let {
            toDate().time + OnlineTSUpdater.EXPIRE_STATUS_INTERVAL >= currentTimeMillis()
        } ?: false

    }


    private fun subscribeForGroupNotification() {
        execute(false) {

            channelsState.value().filter {
                it.type == Channel.Type.Group
            }.forEach { channel ->
                Firebase.messaging.subscribeToTopic(channel.id()).await()
            }
        }
    }
}
