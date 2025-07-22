package com.example.mychatapp.domain.remote

import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.domain.model.Message
import com.example.mychatapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ChannelRepo {
    suspend fun getOneToOneChat(currentUserId: String, otherUserId: String): Channel?
    suspend fun createOneToOneChannel(currentUserId: String, otherUserId: String): String
    suspend fun getAllChannels(currentUser: String, users: List<User>) : List<Channel>
    suspend fun getChannel(channelId :String) : Channel
    suspend fun sendMessage(channelId: String,message : Message)
    suspend fun getChannelWithFlowMessage(channelId: String, users: List<User>, id: String) : Flow<Channel>
    suspend fun createGroupChannel(currentUserId: String, name: String, description: String, groupImage : String?, members : List<String>,): String

}