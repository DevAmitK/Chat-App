package com.example.mychatapp.presentation.newGroupChat.comp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mychatapp.domain.ext.id
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.ui.comp.UserCard


fun LazyListScope.MembersInput(userList: List<User>, members: SnapshotStateList<String>) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
            text = "Members",
            style = MaterialTheme.typography.titleMedium
        )
    }
    items(userList) { user ->

        UserCard(
            user = user,
            checked = members.contains(user.id()),
            onCheckedChange = { checked ->
                if (checked) members.add(user.id()) else members.remove(user.id())
            },
            onClick = {
                if (members.contains(user.id())) {
                    members.remove(user.id())
                } else {
                    members.add(user.id())
                }

            }
        )
    }

}