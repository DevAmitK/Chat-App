package com.example.mychatapp.presentation.userProfileScreen

import androidx.compose.runtime.mutableStateOf
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.domain.usecase.LastOnlineTSFetcher
import com.example.mychatapp.presentation.homeScreen.HomeViewModel
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userRepo: UserRepo,
    private val channelRepo: ChannelRepo,
    private val lastOnlineTSFetcher: LastOnlineTSFetcher,
) : BaseViewModel(){

    val userOnlineStatus = mutableStateOf<Map<String, Boolean>>(emptyMap())
    data class UserAndGroupInfo(
        val channel: Channel?,
        val members: List<User?>,
        val user: User?
    )
    val userAndGroupInfo = taskStateOf<UserAndGroupInfo>()
    fun getUser(
        channelId: String?
    ){
        execute {
            channelId?.let {

                val channel = channelRepo.getChannel(channelId)
                if (channel.type == Channel.Type.OneToOne) {
                    userAndGroupInfo.load {
                        val otherUserId = channel.members.filter {it != currentUserId() }
                        val user =userRepo.getUserById(otherUserId.first())
                        UserAndGroupInfo(
                            user = user,
                            channel = null,
                            members = emptyList()
                        )
                    }
                }
                else{
                    userAndGroupInfo.load {
                        val groupUser = userRepo.getAllUser().filter {
                            it.id in channel.members
                        }
                        UserAndGroupInfo(
                            user = null,
                            channel = channel,
                            members = groupUser
                        )
                    }
                }
                launch {
                    checkOnlineStatusOfUsers()
                }
            }
        }
    }
    private suspend fun checkOnlineStatusOfUsers() {
        lastOnlineTSFetcher.getOnlineStatusOfAllUser().collectLatest {map->
            userOnlineStatus.value = map
        }
    }


}