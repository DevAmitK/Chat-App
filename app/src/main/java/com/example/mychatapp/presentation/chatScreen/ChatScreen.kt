package com.example.mychatapp.presentation.chatScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mychatapp.ui.comp.LoadingCPI
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.CenterText
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.ifValidInput
import com.streamliners.compose.comp.textInput.state.update

@Composable
fun ChatScreen(
    navHostController: NavHostController,
    channelId: String,
    viewModel: ChatViewModel,
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.start(channelId)
    }

    TitleBarScaffold(title = "Chat",navigateUp = {
        navHostController.navigateUp()
    }) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(paddingValue)

        ) {

            val massageInput = remember {
                mutableStateOf(
                    TextInputState("Massage")
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                viewModel.data.whenLoading {
                    LoadingCPI(modifier = Modifier.fillMaxSize())
                }
                viewModel.data.whenLoaded {
                    if ( it.channel.messages.isNotEmpty()) {
                        MassageList(it)
                    }else{
                        CenterText(text = "Chat Is Empty")
                    }
                }
            }
            TextInputLayout(
                state = massageInput,
                trailingIconButton = {
                    IconButton(onClick = {
                        massageInput.ifValidInput { massage->
                            viewModel.sendMessage(massage,channelId = channelId, onSuccess = {
                                massageInput.update("")
                            })
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}










