package com.example.mychatapp.presentation.newChatsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.presentation.navigation.Routes
import com.example.mychatapp.ui.comp.LoadingCPI
import com.example.mychatapp.ui.comp.UserCard
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.CenterText

@Composable
fun NewChatScreen(
    chatViewModel: NewChatViewModel,
    navHostController: NavHostController,
) {
    LaunchedEffect(key1 = Unit) {
        chatViewModel.fetchUsers()
    }
    TitleBarScaffold(title = "New Chat", navigateUp = {
        navHostController.navigateUp()
    }) {

        chatViewModel.usersListTask.whenLoaded { userList ->
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (userList.isEmpty()) {
                   item{
                       CenterText(text = "Empty...")
                   }
                } else {
                items(userList) { user ->
                    UserCard(
                        user = user,
                        onClick = {
                            chatViewModel.onUserSelected(
                                otherUserId = user.id(),
                                onChannelReady = {
                                    navHostController.navigate(Routes.ChatScreen(it))
                                }
                            )
                        }
                    )
                }
                }
            }
        }
        chatViewModel.usersListTask.whenLoading {
            LoadingCPI(modifier = Modifier.fillMaxSize())
        }
    }
}

