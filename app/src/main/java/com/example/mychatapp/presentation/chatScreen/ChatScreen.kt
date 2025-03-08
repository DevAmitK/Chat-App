package com.example.mychatapp.presentation.chatScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.ui.comp.AsyncImage
import com.example.mychatapp.ui.comp.ImageState
import com.example.mychatapp.ui.comp.LoadingCPI
import com.mr0xf00.easycrop.AspectRatio
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.base.taskState.valueNullable
import com.streamliners.compose.comp.CenterText
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.ifValidInput
import com.streamliners.compose.comp.textInput.state.update
import com.streamliners.pickers.media.FromGalleryType
import com.streamliners.pickers.media.MediaPickerCropParams
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.MediaPickerDialogState
import com.streamliners.pickers.media.MediaType
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navHostController: NavHostController,
    channelId: String,
    viewModel: ChatViewModel,
) {
    val mediaPickerDialogState = rememberMediaPickerDialogState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.start(channelId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val data = remember {
                            derivedStateOf {
                                viewModel.data.valueNullable()
                            }
                        }
                        AsyncImage(
                            uri =data.value?.channel?.imageUrl ?: "" ,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .run {
                                    // TODO : Try Showing Green Dot in On top of the profile image for Online Status
                                    if (
                                        data.value?.isOtherUserOnline == true
                                    ) {
                                        border(
                                            width = 5.dp,
                                            color = Color.Green,
                                            shape = CircleShape
                                        )
                                    } else this
                                },
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = data.value?.channel?.name ?: "Chat")
                    }
                }
            )
        },

        ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(paddingValue)

        ) {

            val messageInput = remember {
                mutableStateOf(
                    TextInputState("Message")
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                viewModel.data.whenLoading {
                    LoadingCPI(modifier = Modifier.fillMaxSize())
                }
                viewModel.data.whenLoaded {
                    if (it.channel.messages.isNotEmpty()) {
                        MessageList(it)
                    } else {
                        CenterText(text = "Chat Is Empty")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        mediaPickerDialogState.value = MediaPickerDialogState.ShowMediaPicker(
                            type = MediaType.Image,
                            allowMultiple = false,
                            fromGalleryType = FromGalleryType.VisualMediaPicker,
                            cropParams =
                            MediaPickerCropParams.Enabled(
                                showAspectRatioSelectionButton = false,
                                showShapeCropButton = false,
                                lockAspectRatio = AspectRatio(1, 1)
                            ),
                            callback = { getList ->
                                scope.launch(
                                    Dispatchers.IO
                                ) {
                                    val list = getList()
                                    list.firstOrNull()?.let {
                                        viewModel.sendImage(
                                            uri = ImageState.New(it),
                                            channelId = channelId)
                                    }
                                }
                            }
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_image_24),
                        contentDescription = "Send Image"
                    )

                }

                TextInputLayout(
                    state = messageInput,
                    trailingIconButton = {
                        IconButton(onClick = {
                            messageInput.ifValidInput { message ->
                                viewModel.sendMessage(message, channelId = channelId, onSuccess = {
                                    messageInput.update("")
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
    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.example.mychatapp.fileprovider"
    )
}









