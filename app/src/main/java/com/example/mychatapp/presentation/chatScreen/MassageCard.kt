package com.example.mychatapp.presentation.chatScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mychatapp.domain.model.Message
import com.streamliners.utils.DateTimeUtils



@Composable
fun MassageCard(massage: Message) {
    Card{
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = massage.message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            val formattedTime = remember {
                derivedStateOf {
                    DateTimeUtils.formatTime(
                        DateTimeUtils.Format.HOUR_MIN_12,
                        massage.time.toDate().time
                    )
                }
            }
            Text(
                text = formattedTime.value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
    }
}


