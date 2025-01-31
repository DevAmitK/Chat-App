package com.example.mychatapp.presentation.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychatapp.domain.model.Channel
import com.streamliners.compose.comp.CenterText


@Composable
fun ChannelList(listOfChannel: List<Channel>, navHostController: NavHostController) {

    LazyColumn(
        modifier = Modifier.padding(1.dp),
        contentPadding = PaddingValues(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        if (listOfChannel.isEmpty()) {
            item {
                CenterText(text = "Empty...")
            }
        } else {
            items(listOfChannel) { channel ->
                ChannelCard(channel,navHostController)
            }
        }
    }
}