package com.example.mychatapp.presentation.chatScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.ui.comp.ImageState
import com.example.mychatapp.ui.comp.LoadingCPI
import com.mr0xf00.easycrop.AspectRatio
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
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

    TitleBarScaffold(title = "Chat", navigateUp = {
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
                modifier = Modifier.weight(1f).padding(10.dp)
            ) {
                viewModel.data.whenLoading {
                    LoadingCPI(modifier = Modifier.fillMaxSize())
                }
                viewModel.data.whenLoaded {
                    if (it.channel.messages.isNotEmpty()) {
                        MassageList(it)
                    } else {
                        CenterText(text = "Chat Is Empty")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
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
                    state = massageInput,
                    trailingIconButton = {
                        IconButton(onClick = {
                            massageInput.ifValidInput { massage ->
                                viewModel.sendMessage(massage, channelId = channelId, onSuccess = {
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
    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.example.mychatapp.fileprovider"
    )
}









