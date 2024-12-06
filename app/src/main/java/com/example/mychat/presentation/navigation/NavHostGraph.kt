package com.example.mychat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mychat.presentation.EditProfile.EditProfileScreen
import com.example.mychat.presentation.Login.LoginScreen
import com.example.mychat.presentation.chatSceen.ChatScreen
import com.example.mychat.presentation.home.Home
import com.example.mychat.presentation.newChatsScreen.NewChatScreen
import com.example.mychat.presentation.splashScreen.SplashScreen
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
            val channelId: Routes.ChatScreen = it.toRoute()
            ChatScreen(
                navHostController = navHostController,
                channelId = channelId.channelId,
                viewModel = koinBaseViewModel()
            )
        }
    }
}