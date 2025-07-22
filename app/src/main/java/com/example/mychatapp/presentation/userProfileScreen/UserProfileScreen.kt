package com.example.mychatapp.presentation.userProfileScreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.ui.comp.ProfileImagePicker
import com.mr0xf00.easycrop.AspectRatio
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.pickers.media.FromGalleryType
import com.streamliners.pickers.media.MediaPickerCropParams
import com.streamliners.pickers.media.MediaPickerDialogState
import com.streamliners.pickers.media.MediaType
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun UserProfileScreen(navHostController: NavHostController, viewModel: UserProfileViewModel) {

    var userEditName by remember { mutableStateOf(false) }
    var userEditBio by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val mediaPickerDialogState = rememberMediaPickerDialogState()
    val scope = rememberCoroutineScope()

    TitleBarScaffold(title = "Profile Screen",
        navigateUp = {
            navHostController.navigateUp()
        }){

        LaunchedEffect(key1 = Unit) {
            viewModel.getUser()
        }

        viewModel.user.whenLoaded {userData->
            if (userData != null) {
                var userName by remember { mutableStateOf(userData.name) }
                var userBio by remember { mutableStateOf( userData.bio ?: "No Bio")}
                val userEmail by remember { mutableStateOf(userData.email) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(modifier = Modifier.padding(10.dp)) {
                        Card(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(shape = CircleShape),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                        ) {
                            ProfileImagePicker(
                                defaultIconResId = R.drawable.person_24,
                                imageUri = userData.imageUri) {
                                /**
                                 * TODO View Image in a Dialog
                                 */
                            }
                        }
                        Icon(
                            imageVector = Icons.Rounded.AddCircle, contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(40.dp)
                                .clickable {
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
                                                    //imageUri = it
                                                }
                                            }
                                        }
                                    )
                                },
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                        OutlinedTextField(value = userName, onValueChange = {
                            userName = it
                        }, label = {
                            Text(text = "Name")
                        },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(imageVector =
                                if (!userEditName) {
                                    Icons.Default.Edit
                                } else {
                                    Icons.Default.Done
                                }, contentDescription = "",
                                    modifier = Modifier.clickable {
                                        userEditName = !userEditName
                                        if (!userEditName) {
                                            val updatedUser = userData.copy(
                                                name = userName
                                            )

                                            viewModel.updateUserData(updatedUser)
                                            Toast.makeText(context, userName, Toast.LENGTH_SHORT).show()

                                        }
                                    })
                            },
                            enabled = userEditName
                        )

                        OutlinedTextField(
                            value = userBio, onValueChange = {
                                userBio=it
                            },
                            label = {
                                Text(text = "Bio")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(if (!userEditBio) {
                                    Icons.Default.Edit
                                } else {
                                    Icons.Default.Done
                                }, contentDescription = "",
                                    modifier = Modifier.clickable {
                                        userEditBio = !userEditBio
                                        if (!userEditBio) {
                                            val updatedUser = userData.copy(
                                                bio = userBio
                                            )
                                            viewModel.updateUserData(updatedUser)

                                        }
                                    })
                            },
                            enabled = userEditBio
                        )

                        OutlinedTextField(
                            value = userEmail, onValueChange = {},
                            label = {
                                Text(text = "Email")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                    }
                }
            }else{


            }
        }
    }
}
