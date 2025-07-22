package com.example.mychatapp.presentation.userProfileScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mychatapp.R
import com.example.mychatapp.domain.model.User

@Composable
fun InfoUi(
    imageUri: String?,
    name: String,
    bio: String?,
    email: String?,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .size(100.dp)
                .clip(shape = CircleShape),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
        ) {
            AsyncImage(
                model = imageUri ?: R.drawable.person_24, contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            OutlinedTextField(value = name, onValueChange = {}, label = {
                Text(text = "Name")
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            OutlinedTextField(
                value = bio ?: "", onValueChange = {},
                label = {
                    Text(text = "Bio")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            email?.let {
                OutlinedTextField(
                    value = email, onValueChange = {},
                    label = {
                        Text(text = "Email")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
            }
        }
    }
}