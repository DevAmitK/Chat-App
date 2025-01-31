package com.example.mychatapp.ui.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.ui.theme.lightBlue

@Composable
fun UserCard(
    user: User,
    onClick:() -> Unit,
) {

    Card(onClick = onClick ,
        modifier = Modifier.padding(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = lightBlue
        ),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImages(
                uri = user.imageUri ?: placeHolder(user.name),
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),

                )
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }

        }

    }

}
