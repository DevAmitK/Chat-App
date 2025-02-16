package com.example.mychatapp.presentation.newGroupChat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.presentation.navigation.Routes
import com.example.mychatapp.presentation.newGroupChat.comp.GroupInfoInput
import com.example.mychatapp.presentation.newGroupChat.comp.MembersInput
import com.example.mychatapp.ui.comp.ImageState
import com.example.mychatapp.ui.comp.LoadingCPI
import com.example.mychatapp.ui.comp.placeHolder
import com.example.mychatapp.ui.theme.floatingActionButton
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.compose.android.comp.appBar.TitleBar
import com.streamliners.compose.comp.textInput.config.InputConfig
import com.streamliners.compose.comp.textInput.config.text
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.ifValidInput
import com.streamliners.pickers.media.PickedMedia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGroupChatScreen(
    chatViewModel: NewGroupChatViewModel,
    navHostController: NavHostController,
) {
    val image = remember {
        mutableStateOf<ImageState?>(ImageState.Empty)
    }
    val nameInput = remember {
        mutableStateOf(
            TextInputState(
                label = "Group Name"
            )
        )
    }
    val groupDescriptionInput = remember {
        mutableStateOf(
            TextInputState(
                label = "Group Description",
                inputConfig = InputConfig.text {
                    optional = true
                }
            )
        )
    }
    val members = remember {
        mutableStateListOf<String>()
    }

    LaunchedEffect(key1 = Unit) {
        chatViewModel.fetchUsers()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleBar(
                title = "New Group Chat",
                navigateUp = {}
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    nameInput.ifValidInput {
                        chatViewModel.createGroupChannel(
                            name = nameInput.value.value,
                            groupImage =image.value,
                            description = groupDescriptionInput.value.value,
                            members = members,
                            onChannelReady = {channelId->
                                navHostController.navigate(Routes.ChatScreen(channelId)){

                                }
                            }
                        )
                    }
                },
                containerColor = floatingActionButton,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_groups_2_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(text = "Create Group")

            }
        }
    ) {


//TODO Avoid hiding entire Screen when the members List load
        chatViewModel.usersListTask.whenLoaded { userList ->
            LazyColumn (
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(
                    bottom = 65.dp
                )
            ){
                item {
                    GroupInfoInput(image, nameInput, groupDescriptionInput)
                }
                MembersInput(userList,members)
            }
        }
    }
    chatViewModel.usersListTask.whenLoading {
        LoadingCPI(modifier = Modifier.fillMaxSize())
    }
}




