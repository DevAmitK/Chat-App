package com.example.mychatapp.presentation.splashScreen

import androidx.navigation.NavHostController
import com.example.mychatapp.domain.local.repo.PreferenceRepo
import com.example.mychatapp.presentation.navigation.Routes
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val repo: PreferenceRepo,
) : BaseViewModel() {


    fun checkUserIsLoginOrNot(
        navHostController: NavHostController,
    ) {
        execute(showLoadingDialog = false) {

            val isLoggedIn = repo.getLoginState()
            delay(2000)
            withContext(Dispatchers.Main) {
                if (isLoggedIn){
                    navHostController.navigate(Routes.HomeScreen) {
                        popUpTo(Routes.SplashScreen) { inclusive = true }
                    }
                }
                else{
                    navHostController.navigate(Routes.LoginScreen) {
                        popUpTo(Routes.SplashScreen) { inclusive = true }
                    }
                }
            }
        }
    }
}