package com.example.mychatapp.domain.remote

import com.example.mychatapp.domain.model.User


interface UserRepo {


    suspend fun saveUserData(user: User)
    suspend fun getUserWithEmail(email: String): User?
    suspend fun getAllUser(): List<User>
}