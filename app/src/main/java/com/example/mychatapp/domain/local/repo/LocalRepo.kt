package com.example.mychatapp.domain.local.repo

interface LocalRepo {
    suspend fun getLoginState() : Boolean
    suspend fun saveLoginState(isLogin : Boolean)
}