package com.example.mychatapp.ui.comp

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mychatapp.domain.model.User

@Composable
fun UserCard(
    user: User,
    onClick: () -> Unit,
    checked: Boolean? = null,
    onCheckedChange: (Boolean) -> Unit = {},
) {

    Column(
        modifier = Modifier
            .padding(3.dp)
            .clickable {
                onClick.invoke()
            },
        ){
            Row(
                verticalAlignment  = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        uri = user.imageUri ?: placeHolder(user.name),
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),

                        )
                    Column {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }

                }

                checked?.let {
                    Checkbox(checked =it, onCheckedChange = onCheckedChange)
                }
            }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(MaterialTheme.colorScheme.onSecondary)
                )

        }
    }
