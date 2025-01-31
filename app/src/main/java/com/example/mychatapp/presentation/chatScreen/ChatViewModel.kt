package com.example.mychatapp.presentation.chatScreen

import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.Message
import com.example.mychatapp.domain.remote.ChannelRepo
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repo: ChannelRepo,
) : BaseViewModel() {
    val channel = taskStateOf<Channel>()

    fun start(channelId: String) {
        execute(
            showLoadingDialog = false
        ) {
            launch {
                repo.getChannelWithFlowMessage(channelId).collectLatest {
                    channel.update(it)
                }
            }

        }

    }

    fun sendMessage(messageStr: String, channelId: String, onSuccess :() ->Unit) {
        val message = Message(
            message = messageStr,
            sender = currentUserId(),
            mediaUrl = null
        )

        execute(showLoadingDialog = false) {
            repo.sendMassage(channelId = channelId, message =message)
            onSuccess()
        }
    }
}