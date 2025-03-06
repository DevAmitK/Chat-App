package com.example.mychatapp.domain.usecase

import com.example.mychatapp.domain.Constants.OnlineTSUpdater
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.remote.UserRepo
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.System.currentTimeMillis

class LastOnlineTSFetcher(
    private val userRepo: UserRepo,
) {
    private val userOnlineStatus = mapOf<String, Boolean>()

    suspend fun getOnlineStatusOfAllUser(): Flow<Map<String, Boolean>> {
        return userRepo.getAllUserFlow().map { users ->
            userOnlineStatus.toMutableMap().apply {
                users.forEach {user->
                    put(user.id(),user.lastOnlineTS.isOnline())
                }
            }
        }
    }

    fun Timestamp?.isOnline(): Boolean {
        return this?.let {
            toDate().time + OnlineTSUpdater.EXPIRE_STATUS_INTERVAL >= currentTimeMillis()
        } ?: false

    }
}