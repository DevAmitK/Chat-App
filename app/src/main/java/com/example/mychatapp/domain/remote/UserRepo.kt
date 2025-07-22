package com.example.mychatapp.domain.remote

import com.example.mychatapp.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserRepo {


    suspend fun saveUserData(user: User)
    suspend fun getUserWithEmail(email: String): User?
    suspend fun getUserById(id: String): User
    suspend fun getAllUser(): List<User>
    suspend fun getAllUserFlow(): Flow<List<User>>
    suspend fun updateFcmToken(fcmToken : String,userId : String)
    suspend fun updateLastOnlineTS(userId : String)
}