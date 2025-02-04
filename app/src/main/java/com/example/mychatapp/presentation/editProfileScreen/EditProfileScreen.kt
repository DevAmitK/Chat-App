package com.example.mychatapp.presentation.editProfileScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.presentation.common.CustomEditText
import com.example.mychatapp.presentation.navigation.Routes
import com.example.mychatapp.ui.comp.CustomDialog
import com.example.mychatapp.ui.comp.ProfileImagePicker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.mr0xf00.easycrop.AspectRatio
import com.streamliners.base.taskState.comp.whenLoading
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
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
fun EditProfileScreen(
    navHostController: NavHostController,
    viewModel: EditProfileViewModel,
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // ViewModel states
    var name by remember { mutableStateOf(auth.currentUser?.displayName.orEmpty()) }
    var email by remember { mutableStateOf(auth.currentUser?.email.orEmpty()) }
    var bio by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var bioError by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<PickedMedia?>(null) }
    var imageUriString by remember { mutableStateOf<String?>(null) }
    var selectedGender by remember { mutableStateOf<User.Gender?>(null) }
    var userUidNotFound by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val userData by viewModel.userState.collectAsState()

    // Gender options
    val genderOptions = User.Gender.entries.map { it.name }
    val mediaPickerDialogState = rememberMediaPickerDialogState()

    // Check if user is authenticated
    LaunchedEffect(Unit) {
        userUidNotFound = auth.currentUser?.uid.isNullOrEmpty()
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.loadUser()
    }

    // Show dialog if user UID is not found
    if (userUidNotFound) {
        CustomDialog(
            showDialog = true,
            title = "Login Failed",
            message = "Please log in again.",
            confirmButtonText = "Retry",
            onConfirm = {
                val googleSignInClient =
                    GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                googleSignInClient.signOut().addOnCompleteListener {
                    navHostController.navigate(Routes.LoginScreen){
                        popUpTo(Routes.EditProfileScreen) { inclusive = true }
                    }
                }
            }
        )
    }

    TitleBarScaffold(title = "Edit Profile") { padding ->
        LaunchedEffect(key1 = userData) {
            userData?.let {
                name = it.name
                bio = it.bio ?: ""
                email = it.email
                selectedGender = it.gender
                imageUriString = it.imageUri
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Profile image picker
                Card {
                    ProfileImagePicker(
                        defaultIconResId = R.drawable.person_24,
                        imageUri = imageUriString,
                        imagePickMedia = imageUri
                    ) {
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
                                        imageUri = it
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name input
                CustomEditText(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { inputName ->
                        name = inputName
                        nameError = inputName.isBlank()
                    },
                    label = "Name",
                    isError = nameError,
                    maxLines = 1,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    },
                    errorMessage = "Name is required"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email display (read-only)
                CustomEditText(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = {},
                    label = "Email",
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
                    },
                    enabled = false
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Bio input
                CustomEditText(
                    modifier = Modifier.fillMaxWidth(),
                    value = bio,
                    onValueChange = { inputBio ->
                        bio = inputBio
                        bioError = inputBio.isBlank()
                    },
                    label = "Bio",
                    isError = bioError,
                    maxLines = 3,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    },
                    errorMessage = "Bio is required"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Gender selection
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Gender", modifier = Modifier.padding(bottom = 8.dp))

                    genderOptions.forEach { genderName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedGender = User.Gender.valueOf(genderName)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Radio Button
                            RadioButton(
                                selected = selectedGender?.name == genderName,
                                onClick = {
                                    selectedGender = User.Gender.valueOf(genderName)
                                }
                            )

                            // Gender Label
                            Text(
                                text = genderName,
                                modifier = Modifier.padding(start = 8.dp),
                                color = if (selectedGender?.name == genderName) MaterialTheme.colorScheme.onSecondary else Color.Black
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                // Save button
                ElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                    onClick = {
                        /**
                         * TODO After creating the profile, when I am
                         * going to edit the profile, if I do not change
                         * the photo while saving, then the photo is showing
                         * NULL
                         */
                        if (name.isNotBlank()) {
                            val user = User(
                                id = currentUserId(),
                                name = name,
                                email = email,
                                bio = bio.takeIf { it.isNotBlank() },
                                gender = selectedGender,
                                imageUri = imageUri?.uri,
                                fcmToken = null
                            )

                            viewModel.saveUser(
                                user,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        "Profile Create successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navHostController.navigate(Routes.HomeScreen) {
                                        popUpTo(Routes.EditProfileScreen) { inclusive = true }
                                    }
                                },
                            )
                        } else {
                            nameError = true
                        }
                    }
                ) {
                    Text(color = Color.White, text = "Save")
                }
            }

            viewModel.saveProfileTask.whenLoading {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.example.mychatapp.fileprovider"
    )
}
