package com.example.mychatapp.presentation.userProfileScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.mychatapp.R
import com.example.mychatapp.ui.comp.placeHolder
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold


@Composable
fun UserProfileScreen(
    navHostController: NavHostController,
    viewModel: UserProfileViewModel,
    channelId: String?,

    ) {


    TitleBarScaffold(title = "Profile Screen",
        navigateUp = {
            navHostController.navigateUp()
        }
    ) {

        LaunchedEffect(key1 = Unit) {
            viewModel.getUser(
                channelId
            )
        }

        viewModel.user.whenLoaded { userData ->
            if (userData != null) {
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
                            AsyncImage(
                                model = userData.imageUri ?: R.drawable.person_24
                                , contentDescription = null)
                        }

                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                        OutlinedTextField(value = userData.name, onValueChange = {}, label = {
                            Text(text = "Name")
                        },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )

                        OutlinedTextField(
                            value = userData.bio ?: "", onValueChange = {},
                            label = {
                                Text(text = "Bio")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )

                        OutlinedTextField(
                            value = userData.email, onValueChange = {},
                            label = {
                                Text(text = "Email")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "User Not Found")
                }
            }
        }
    }
}
