package com.example.mychatapp.presentation.newGroupChat

import androidx.core.net.toUri
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.StorageRepo
import com.example.mychatapp.ui.comp.placeHolder
import com.streamliners.base.BaseViewModel
import com.streamliners.base.exception.failure
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.pickers.media.PickedMedia
import javax.inject.Inject


class NewGroupChatViewModel @Inject constructor(
    private val channelRepo: ChannelRepo,
    private val userRepo: UserRepo,
    private val storageRepo: StorageRepo,
) : BaseViewModel() {

    val usersListTask = taskStateOf<List<User>>()

    fun fetchUsers() {
        execute(showLoadingDialog = false) {
            usersListTask.load {
                userRepo.getAllUser().filter { it.id() != currentUserId() }
            }
        }
    }

    fun createGroupChannel(
        name: String,
        description: String,
        groupImage : PickedMedia?,
        members : List<String>,
        onChannelReady: (String) -> Unit
    ) {

        execute(showLoadingDialog = false) {
            if (members.size < 2) failure("Members must be two or more then two")
            val imageUrl =   groupImage?.let {
                storageRepo.uploadFile("groupImages/$name-${System.currentTimeMillis()}", it.uri.toUri())
            } ?: placeHolder(name = name)

            val channelId = channelRepo.createGroupChannel(
                name = name,
                groupImage = imageUrl,
                members = members,
                description = description,
                currentUserId = currentUserId()
            )

            executeOnMain {
                onChannelReady(channelId)
            }
        }
    }

}


