package com.example.mychatapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mychatapp.presentation.editProfileScreen.EditProfileScreen
import com.example.mychatapp.presentation.loginScreen.LoginScreen
import com.example.mychatapp.presentation.chatScreen.ChatScreen
import com.example.mychatapp.presentation.homeScreen.Home
import com.example.mychatapp.presentation.newChatsScreen.NewChatScreen
import com.example.mychatapp.presentation.splashScreen.SplashScreen
import com.example.mychatapp.presentation.userProfileScreen.UserProfileScreen
import com.streamliners.base.BaseActivity
import com.streamliners.base.ext.koinBaseViewModel

@Composable
fun BaseActivity.NavHostGraph() {

    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = Routes.SplashScreen){

        composable<Routes.SplashScreen> {
            SplashScreen(navHostController, viewModel = koinBaseViewModel())
        }

        composable<Routes.LoginScreen> {
            LoginScreen(navHostController, viewModel = koinBaseViewModel())
        }

        composable<Routes.EditProfileScreen> {
            EditProfileScreen(navHostController, viewModel = koinBaseViewModel())
        }

        composable<Routes.HomeScreen> {
            Home(navHostController, viewModel = koinBaseViewModel())
        }

        composable<Routes.NewChatScreen> {

            NewChatScreen(navHostController = navHostController, chatViewModel = koinBaseViewModel())
        }

        composable<Routes.ChatScreen> {
            val route: Routes.ChatScreen = it.toRoute()
            ChatScreen(
                navHostController = navHostController,
                channelId = route.channelId,
                viewModel = koinBaseViewModel()
            )
        }
        composable<Routes.UserProfileScreen> {

            UserProfileScreen(navHostController = navHostController, viewModel = koinBaseViewModel())
        }

    }
}