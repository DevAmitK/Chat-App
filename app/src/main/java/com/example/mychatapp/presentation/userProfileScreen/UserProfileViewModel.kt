package com.example.mychatapp.presentation.userProfileScreen

import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.UserRepo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf

class UserProfileViewModel(
    private val repo: UserRepo
) : BaseViewModel(){

    val user = taskStateOf<User?>()
    fun getUser(){
        execute {
            val userEmail = Firebase.auth.currentUser?.email
            user.load {
                if (userEmail != null) {
                    repo.getUserWithEmail(userEmail)
                }else{
                    null
                }
            }
        }
    }

    fun updateUserData(user : User){
        execute {
            repo.saveUserData(user)
        }
    }
}