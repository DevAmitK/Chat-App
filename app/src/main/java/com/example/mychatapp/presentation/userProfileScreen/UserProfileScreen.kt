package com.example.mychatapp.presentation.userProfileScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mychatapp.presentation.homeScreen.ChannelCard
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold


@Composable
fun UserProfileScreen(
    navHostController: NavHostController,
    viewModel: UserProfileViewModel,
    channelId: String?,
    ) {
    TitleBarScaffold(title = "Profile Screen",
        navigateUp = {
            navHostController.navigateUp()
        }
    ) {

        LaunchedEffect(key1 = Unit) {
            viewModel.getUser(
                channelId
            )
        }
        Column(
            modifier = Modifier.padding(it)
        ) {
            viewModel.userAndGroupInfo.whenLoaded { userAndGroupInfo ->

                if (userAndGroupInfo.user != null) {

                    InfoUi(
                        name = userAndGroupInfo.user.name,
                        email = userAndGroupInfo.user.email,
                        imageUri = userAndGroupInfo.user.imageUri,
                        bio = userAndGroupInfo.user.bio
                    )

                } else if (userAndGroupInfo.members.isNotEmpty()) {
                    InfoUi(
                        name = userAndGroupInfo.channel?.name ?: "",
                        email = null,
                        imageUri = userAndGroupInfo.channel?.imageUrl ?: "",
                        bio = userAndGroupInfo.channel?.description ?: ""
                    )
                    LazyColumn {
                        items(userAndGroupInfo.members) {user ->
                            if (user != null) {
                                ChannelCard(
                                    imageUrl = user.imageUri ?: "",
                                    name =user.name,
                                    onClick = {},
                                    isOnline = true
                                )
                            }
                        }
                    }

                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "User Not Found")
                    }
                }
            }
        }

    }
}


