package com.example.mychatapp.presentation.loginScreen

import androidx.navigation.NavHostController
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.local.repo.LocalRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.presentation.navigation.Routes
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val preferenceRepo: LocalRepo,
) : BaseViewModel() {

    /**
     *  check user is login before or not if login the go to home screen
     *  or Edit Screen
     */

    fun checkIfUserExistsAndLogin(
        email: String,
        navHostController: NavHostController,
    ) {
        execute (showLoadingDialog = false){
            var user = userRepo.getUserWithEmail(email)

            //Subscribe to FCM topic
            Firebase.messaging.subscribeToTopic("general")

            executeOnMain {
                if (user != null) {
                    preferenceRepo.saveLoginState(true) // Save login state

                    //update Firebase Token
                    val token = Firebase.messaging.token.await()
                    user = user!!.copy(fcmToken = token)
                    userRepo.updateFcmToken(token, currentUserId())

                    //TODO User Save in Local


                    navHostController.navigate(Routes.HomeScreen) {
                        popUpTo(Routes.LoginScreen) { inclusive = true }
                    }
                } else {
                    navHostController.navigate(Routes.EditProfileScreen) {
                        popUpTo(Routes.LoginScreen) { inclusive = true }
                    }
                }
            }
        }
    }
}