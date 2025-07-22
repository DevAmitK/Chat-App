package com.example.mychatapp.domain.local.repo

interface PreferenceRepo {
    suspend fun getLoginState() : Boolean
    suspend fun saveLoginState(isLogin : Boolean)
}