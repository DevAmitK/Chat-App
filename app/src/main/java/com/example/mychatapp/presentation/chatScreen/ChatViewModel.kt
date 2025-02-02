package com.example.mychatapp.presentation.chatScreen

import androidx.core.net.toUri
import coil3.Uri
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.Message
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.StorageRepo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.utils.DateTimeUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repo: ChannelRepo,
    private val storageRepo: StorageRepo,
) : BaseViewModel() {


    sealed class ChatListItem {
        data class ReceivedMessage(
            val time: String,
            val message: Message,
        ) : ChatListItem()

        data class Date(val date: String) : ChatListItem()

        data class SentMessages(
            val time: String,
            val message: Message,
        ) : ChatListItem()
    }

    data class Data(
        val channel: Channel,
        val chatListItems: List<ChatListItem>,
    )

    val data = taskStateOf<Data>()

    fun start(channelId: String) {
        execute(
            showLoadingDialog = false
        ) {
            launch {
                repo.getChannelWithFlowMessage(channelId).collectLatest {
                    data.update(Data(it,createChatListItems(it, currentUserId())))
                }
            }

        }

    }

    fun sendMessage(
        messageStr: String,
        channelId: String,
        onSuccess :() ->Unit) {
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


    private fun createChatListItems(
        channel: Channel,
        currentUser: String
    ): List<ChatListItem> {

        return buildList {
            var previousDate = ""
            channel.messages.forEach { message ->
                val dateString = DateTimeUtils.formatTime(
                    DateTimeUtils.Format.DATE_MONTH_YEAR_1,
                    message.time.toDate().time
                )
                if (previousDate != dateString) {
                    add(ChatListItem.Date(dateString))
                    previousDate = dateString
                }

                var chatListItem = if (message.sender == currentUser) {
                    ChatListItem.SentMessages(
                        DateTimeUtils.formatTime(
                            DateTimeUtils.Format.HOUR_MIN_12, message.time.toDate().time
                        ), message
                    )
                }else{
                    ChatListItem.ReceivedMessage(
                        DateTimeUtils.formatTime(
                            DateTimeUtils.Format.HOUR_MIN_12, message.time.toDate().time
                        ), message
                    )
                }
                add(chatListItem)
            }
        }
    }

    fun sendImage(uri: String,channelId:String){
        val email = Firebase.auth.currentUser!!.email
        val timestamp = System.currentTimeMillis()
        execute {
           val imageUrl = storageRepo.uploadFile("media/${timestamp}",uri.toUri())

            val message = Message(
                message = "",
                sender = currentUserId(),
                mediaUrl = imageUrl
            )
            repo.sendMassage(channelId = channelId, message =message)

        }
    }
}