package com.example.chatterbox.chat.groups.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatterbox.ChatScreenObject
import com.example.chatterbox.GroupChatRootObject
import com.example.chatterbox.R
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.core.common.getRelativeTime
import com.example.chatterbox.ui.components.RoundImage

@Composable
fun GroupItem(group: Group, currentUserId: String, navController: NavController?, modifier: Modifier = Modifier) {
    val TAG = "GroupItem"

    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navController?.navigate(GroupChatRootObject(groupId = group.id, groupName = group.name))
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RoundImage(
            image = painterResource(R.drawable.google_logo),
            modifier = Modifier
                .size(50.dp),
            showDot = false
        )

        Spacer(modifier.width(25.dp))

        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceAround
        ){
            Text(
                text = group.name,
                fontSize = 17.sp
            )
            val lastMessageUsername = if (group.lastMessageUserId == currentUserId) "You" else group.lastMessageUsername
            Text(
                text = "${lastMessageUsername}: ${group.lastMessage}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }

        Text(
            text = getRelativeTime(group.lastMessageTime).toString()
        )

    }
}

@PreviewLightDark
@Composable
fun GroupItemPreview(modifier: Modifier = Modifier) {

}