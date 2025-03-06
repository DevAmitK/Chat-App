package com.example.mychatapp.domain.usecase

import android.util.Log
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.local.repo.LocalRepo
import com.example.mychatapp.domain.remote.UserRepo

class LastOnlineTSUpdater(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo
) {

    suspend fun updateTS(){
        val userId = currentUserId()
        userRepo.updateLastOnlineTS(userId)

    }
}