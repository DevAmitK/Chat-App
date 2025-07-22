package com.example.mychatapp.domain.ext

import com.example.mychatapp.domain.model.Channel


fun Channel.id(): String {
    return this.id ?: error("Channel ID Not Found")
}


fun Channel.otherUserId(currentUserId : String) : String{
   return members.find { it != currentUserId }
        ?: error("Other User Id Not Found")
}
