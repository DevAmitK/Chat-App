package com.example.mychatapp.presentation.homeScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.domain.usecase.LastOnlineTSFetcher
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    private val channelRepo: ChannelRepo,
    private val userRepo: UserRepo,
    private val lastOnlineTSFetcher: LastOnlineTSFetcher,
):BaseViewModel() {

    val channelsState = taskStateOf<List<Channel>>()
    val userOnlineStatus = mutableStateOf<Map<String, Boolean>>(emptyMap())

     fun start() {
        execute {
            val users = userRepo.getAllUser()
            val channels = channelRepo.getAllChannels(currentUser = currentUserId(),users)

            channelsState.update(channels)
            subscribeForGroupNotification()
            launch {
                checkOnlineStatusOfUsers(users)
            }

        }
    }

    private suspend fun checkOnlineStatusOfUsers(users: List<User>) {
        lastOnlineTSFetcher.getOnlineStatusOfAllUser().collectLatest {map->
            userOnlineStatus.value = map
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

object HomeViewModelInitializer {
    private var hasStarted = false

    fun startOnce(viewModel: HomeViewModel) {
        if (!hasStarted) {
            viewModel.start()
            hasStarted = true
        }
    }
}

