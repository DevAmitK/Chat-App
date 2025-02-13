package com.example.mychatapp.presentation.homeScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.presentation.navigation.Routes
import com.example.mychatapp.ui.comp.LoadingCPI
import com.example.mychatapp.ui.theme.floatingActionButton
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.helpers.NotificationHelper

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navHostController: NavHostController,viewModel: HomeViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Chats") },
//        containerColor = MaterialTheme.colorScheme.primary,
//        contentColor = contentColorFor(MaterialTheme.colorScheme.secondary),
                actions = {
                    Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = null,
                        modifier = Modifier.clickable {
                            navHostController.navigate(Routes.EditProfileScreen)
                        })

                })
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .size(65.dp),
                    onClick = {
                        navHostController.navigate(Routes.NewGroupChatScreen)
                    },
                    containerColor = floatingActionButton,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_groups_2_24),
                        contentDescription = ""
                    )
                }

                FloatingActionButton(
                    modifier = Modifier
                        .size(65.dp),
                    onClick = { navHostController.navigate(Routes.NewChatScreen) },
                    containerColor = floatingActionButton,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            }
        }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            viewModel.channelsState.whenLoading {
                LoadingCPI(modifier = Modifier.fillMaxSize())
            }
            viewModel.channelsState.whenLoaded { listOfChannel ->
                ChannelList(listOfChannel, navHostController)
            }
        }
    }
    NotificationHelper.PermissionsSetup()

}
