package com.example.mychatapp.presentation.userProfileScreen

import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf

class UserProfileViewModel(
    private val userRepo: UserRepo,
    private val channelRepo: ChannelRepo
) : BaseViewModel(){

    val user = taskStateOf<User?>()
    fun getUser(
        channelId: String?
    ){
        execute {
            channelId?.let {
                val channel = channelRepo.getChannel(channelId)
                if (channel.type == Channel.Type.OneToOne) {
                    user.load {
                        val otherUserId = channel.members.filter {it != currentUserId() }
                        userRepo.getUserById(otherUserId.first())
                    }
                }
            }
        }
    }
}