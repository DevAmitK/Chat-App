package com.example.mychatapp.presentation.homeScreen
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.ext.imageUri
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    private val channelRepo: ChannelRepo,
    private val userRepo: UserRepo
):BaseViewModel() {

    val channelsState = taskStateOf<List<Channel>>()

    fun start() {
        execute(showLoadingDialog = false) {
            val users = userRepo.getAllUser()
            val channels = channelRepo.getAllChannels(currentUser = currentUserId())
                .map { channel ->
                    if (channel.type == Channel.Type.OneToOne) {

                        val otherUserId = channel.members.find { it != currentUserId() }
                            ?: error("Other User Id Not Found")

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

            subscribeForGroupNotification()
        }
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
