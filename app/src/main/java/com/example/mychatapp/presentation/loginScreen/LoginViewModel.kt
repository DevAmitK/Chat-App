package com.example.mychatapp.presentation.loginScreen

import androidx.navigation.NavHostController
import com.example.mychatapp.domain.local.repo.PreferenceRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.presentation.navigation.Routes
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import javax.inject.Inject


class LoginViewModel @Inject constructor(
    private val repo: UserRepo,
    private val preferenceRepo: PreferenceRepo,
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
            val user = repo.getUserWithEmail(email)

            executeOnMain {

                if (user != null) {
                    preferenceRepo.saveLoginState(true) // Save login state
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