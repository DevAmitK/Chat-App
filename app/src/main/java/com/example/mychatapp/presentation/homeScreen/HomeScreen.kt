package com.example.mychatapp.presentation.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.presentation.navigation.Routes
import com.example.mychatapp.ui.comp.LoadingCPI
import com.example.mychatapp.ui.theme.floatingActionButton
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold

@Composable
fun Home(navHostController: NavHostController,viewModel: HomeViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }
    TitleBarScaffold(
        title = "Chats",
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = contentColorFor(MaterialTheme.colorScheme.secondary),
         actions = {
        Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = null,
            modifier = Modifier.clickable {
                navHostController.navigate(Routes.EditProfileScreen)
            })
    }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(it)){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                viewModel.channelsState.whenLoading {
                    LoadingCPI(modifier = Modifier.fillMaxSize())
                }
                viewModel.channelsState.whenLoaded { listOfChannel ->
                    ChannelList(listOfChannel,navHostController)
                }
            }
            FloatingActionButton(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                onClick = { navHostController.navigate(Routes.NewChatScreen) },
                containerColor =floatingActionButton,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }

        }
    }
}

