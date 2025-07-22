package com.example.mychatapp.presentation.chatScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageList(data: ChatViewModel.Data) {
    LazyColumn(
       // modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(data.chatListItems) { chatListItem ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = when (chatListItem) {
                    is ChatViewModel.ChatListItem.Date -> {
                        Alignment.Center
                    }

                    is ChatViewModel.ChatListItem.ReceivedMessage -> {
                        Alignment.CenterStart
                    }

                    is ChatViewModel.ChatListItem.SentMessages -> {
                        Alignment.CenterEnd
                    }
                }
            ) {
                when (chatListItem){
                    is ChatViewModel.ChatListItem.Date -> {
                        Text(text = chatListItem.date)

                    }
                    is ChatViewModel.ChatListItem.ReceivedMessage -> {
                        MessageCard(message = chatListItem.message, time = chatListItem.time, senderName = chatListItem.senderName)
                    }
                    is ChatViewModel.ChatListItem.SentMessages -> {
                        MessageCard(message = chatListItem.message, time = chatListItem.time)
                    }
                }
            }
        }
    }
}


