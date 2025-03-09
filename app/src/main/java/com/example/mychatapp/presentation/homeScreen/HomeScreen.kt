package com.example.mychatapp.presentation.homeScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults.containerColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.ext.otherUserId
import com.example.mychatapp.domain.model.Channel
import com.example.mychatapp.presentation.navigation.Routes
import com.example.mychatapp.ui.comp.LoadingCPI
import com.example.mychatapp.ui.theme.floatingActionButton
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.compose.comp.CenterText
import com.streamliners.helpers.NotificationHelper

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navHostController: NavHostController,viewModel: HomeViewModel) {

    LaunchedEffect(Unit) {
        HomeViewModelInitializer.startOnce(viewModel)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = { Text(text = "Chats") },
                actions = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            navHostController.navigate(Routes.EditProfileScreen)
                        }
                    )
                }
            )
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
            Log.d("DEBUG_APP", "Home Loaded")

            viewModel.channelsState.whenLoading {
                LoadingCPI(modifier = Modifier.fillMaxSize())
            }

            viewModel.channelsState.whenLoaded { listOfChannel ->
                Log.d("DEBUG_APP", "Home Loaded : $listOfChannel")
                LazyColumn(
                    modifier = Modifier.padding(1.dp),
                    contentPadding = PaddingValues(1.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {

                    if (listOfChannel.isEmpty()) {
                        item {
                            CenterText(text = "Empty...")

                        }
                    } else {
                        items(listOfChannel) { channel ->
                            ChannelCard(
                              imageUrl = channel.imageUrl ?: "",
                                name = channel.name,
                                onClick = {
                                    navHostController.navigate(Routes.ChatScreen(channel.id()))
                                },
                                isOnline = if (channel.type == Channel.Type.OneToOne) {
                                    val otherUserId= channel.otherUserId(currentUserId())
                                    viewModel.userOnlineStatus.value[otherUserId] ?: false
                                }else false
                            )
                        }
                    }
                }
            }
        }
    }
    NotificationHelper.PermissionsSetup()

}
