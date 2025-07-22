package com.example.mychatapp.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
object Routes {
    @Serializable
    object LoginScreen

    @Serializable
    object EditProfileScreen

    @Serializable
    object HomeScreen

    @Serializable
    object SplashScreen

    @Serializable
    object NewChatScreen

    @Serializable
    object UserProfileScreen

    @Serializable
    data class ChatScreen(val channelId : String)

}