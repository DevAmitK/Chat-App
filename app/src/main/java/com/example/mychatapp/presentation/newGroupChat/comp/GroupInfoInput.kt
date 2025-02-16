package com.example.mychatapp.presentation.newGroupChat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mychatapp.R
import com.example.mychatapp.ui.comp.ImagePicker
import com.example.mychatapp.ui.comp.ImageState
import com.mr0xf00.easycrop.AspectRatio
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.pickers.media.FromGalleryType
import com.streamliners.pickers.media.MediaPickerCropParams
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.MediaPickerDialogState
import com.streamliners.pickers.media.MediaType
import com.streamliners.pickers.media.PickedMedia
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GroupInfoInput(
    image: MutableState<ImageState?>,
    nameInput: MutableState<TextInputState>,
    groupDescriptionInput: MutableState<TextInputState>
) {
    val mediaPickerDialogState = rememberMediaPickerDialogState()


    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ImagePicker(defaultIconResId = R.drawable.group_add_24,
            imageUri = image.value,
            onImageUploadClick = {
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
                                image.value = ImageState.New(it)
                            }
                        }
                    }
                )
            }
        )
        TextInputLayout(state = nameInput)
        TextInputLayout(state = groupDescriptionInput)


    }
    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.example.mychatapp.fileprovider"
    )

}