package com.example.mychatapp.presentation.chatScreen

import androidx.core.net.toUri
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.Message
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.StorageRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.domain.usecase.NewMessageNotifier
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import com.streamliners.utils.DateTimeUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repo: ChannelRepo,
    private val storageRepo: StorageRepo,
    private val userRepo: UserRepo,
    private val newMessageNotifier: NewMessageNotifier
) : BaseViewModel() {


    sealed class ChatListItem {
        data class ReceivedMessage(
            val time: String,
            val message: Message,
            val senderName: String?,
        ) : ChatListItem()

        data class Date(val date: String) : ChatListItem()

        data class SentMessages(
            val time: String,
            val message: Message,
        ) : ChatListItem()
    }

    data class Data(
        val channel: Channel,
        val user: User,
        val chatListItems: List<ChatListItem>,
    )

    val data = taskStateOf<Data>()

    fun start(channelId: String) {
        execute(
            showLoadingDialog = false
        ) {
            //TODO Replace get firebase user to Local user
            val user =userRepo.getUserById(currentUserId())
            launch {
                // TODO Fetch if only a group channel
                val users = userRepo.getAllUser()
                repo.getChannelWithFlowMessage(channelId).collectLatest {
                    data.update(Data(it, user, createChatListItems(it, currentUserId(), users)))
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
            notifyOtherUser(messageStr)
            onSuccess()
        }
    }

    private fun notifyOtherUser(messageString: String) {
        val channel = data.value().channel
        val user = data.value().user
        if (channel.type == Channel.Type.OneToOne) {
            val otherUser = channel.members.find {
                it != currentUserId()
            } ?: error("Other User Not found")


            execute {
                newMessageNotifier.notify(
                    message = messageString,
                    userId =otherUser,
                    userName =user.name
                )
            }
        }
    }


    private fun createChatListItems(
        channel: Channel,
        currentUser: String,
        users: List<User>,
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
                    val name =
                        if (channel.type == Channel.Type.Group) {
                            users.find { it.id == message.sender }?.name
                                ?: error("User Not Found ${message.sender}")
                        } else {
                            null
                        }

                    ChatListItem.ReceivedMessage(
                        DateTimeUtils.formatTime(
                            DateTimeUtils.Format.HOUR_MIN_12, message.time.toDate().time
                        ), message, name
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
            val imageUrl = storageRepo.uploadFile("media/${timestamp}", uri.toUri())

            val message = Message(
                message = "",
                sender = currentUserId(),
                mediaUrl = imageUrl
            )
            repo.sendMassage(channelId = channelId, message =message)
            notifyOtherUser("Send An Image ")

        }
    }
}