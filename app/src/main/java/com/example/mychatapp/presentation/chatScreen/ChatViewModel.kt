package com.example.mychatapp.presentation.chatScreen

import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.ext.otherUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.Channel.Type.Group
import com.example.mychatapp.domain.model.Channel.Type.OneToOne
import com.example.mychatapp.domain.model.Message
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.StorageRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.domain.usecase.LastOnlineTSFetcher
import com.example.mychatapp.domain.usecase.NewMessageNotifier
import com.example.mychatapp.ui.comp.ImageState
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
    private val newMessageNotifier: NewMessageNotifier,
    private val lastOnlineTSFetcher: LastOnlineTSFetcher,
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
        val isOtherUserOnline: Boolean,
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
                repo.getChannelWithFlowMessage(channelId, users, user.id())
                    .collectLatest { channel ->
                        data.update(
                            Data(
                                channel = channel,
                                user = user,
                                isOtherUserOnline = false,
                                chatListItems = createChatListItems(channel, currentUserId(), users)
                            )
                        )
                        if (
                            channel.type == OneToOne
                        ) {
                            listenToOtherUserOnlineStatus(
                                otherUserId = channel.otherUserId(
                                    currentUserId()
                                )
                            )
                        }
                    }
            }
        }
    }

    private fun listenToOtherUserOnlineStatus(
        otherUserId: String,
    ) {
        execute(false) {
            lastOnlineTSFetcher.getOnlineStatusOf(otherUserId).collectLatest {
                data.update(
                    data.value().copy(
                        isOtherUserOnline = it
                    )
                )

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
            repo.sendMessage(channelId = channelId, message =message)
            notifyOtherUser(messageStr)
            onSuccess()
        }
    }


    private fun notifyOtherUser(messageString: String) {
        val channel = data.value().channel
        val user = data.value().user
        when (channel.type) {
            OneToOne -> notifySingleUserByToken(channel, messageString, user)
            Group -> notifyMultipleUserByTopic(channel,messageString,user)
        }
    }


    private fun notifyMultipleUserByTopic(
        channel: Channel, messageString: String,user: User
    ) {
        //TODO Send To all User Except Current User(silently received)
        execute(false) {
            newMessageNotifier.notifyMultipleUsers(
                topic = channel.id(),
                sender = user,
                message = messageString
            )
        }
    }


    private fun notifySingleUserByToken(
        channel: Channel,
        messageString: String,
        user: User,
    ) {
        val otherUser = channel.members.find {
            it != currentUserId()
        } ?: error("Other User Not found")

        execute(false) {
            newMessageNotifier.notifySingleUser(
                message = messageString,
                userId = otherUser,
                userName = user.name
            )
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

                val chatListItem = if (message.sender == currentUser) {
                    ChatListItem.SentMessages(
                        DateTimeUtils.formatTime(
                            DateTimeUtils.Format.HOUR_MIN_12, message.time.toDate().time
                        ), message
                    )
                }else{
                    val name =
                        if (channel.type == Group) {
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


    fun sendImage(
        uri: ImageState,
        channelId:String
    ){
        val timestamp = System.currentTimeMillis()
        execute {
            val imageUrl = storageRepo.uploadFile("media/${timestamp}", uri)

            val message = Message(
                message = "",
                sender = currentUserId(),
                mediaUrl = imageUrl
            )
            repo.sendMessage(channelId = channelId, message =message)
            notifyOtherUser("Send An Image ")
        }
    }

}