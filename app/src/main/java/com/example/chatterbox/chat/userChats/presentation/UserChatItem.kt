package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatterbox.ChatScreenObject
import com.example.chatterbox.R
import com.example.chatterbox.chat.userChats.domain.Member
import com.example.chatterbox.chat.userChats.domain.UserChat
import com.example.chatterbox.core.common.getRelativeTime
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserChatItem(modifier: Modifier = Modifier, userChat: UserChat, navController: NavController?) {
    val TAG = "UserChatItem"
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    val username = if (userChat.members[0].id == currentUserId) userChat.members[1].username
    else userChat.members[0].username

    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navController?.navigate(ChatScreenObject)
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RoundImage(
            image = painterResource(R.drawable.google_logo),
            modifier = Modifier
                .size(50.dp)
        )

        Spacer(modifier.width(25.dp))

        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceAround
        ){
            Text(
                text = username,
                fontSize = 17.sp
            )
            Text(
                text = "${userChat.lastMessageUsername}: ${userChat.lastMessage}"
            )

        }

        Text(
            text = getRelativeTime(userChat.lastMessageTime).toString()
        )

    }
}

@PreviewLightDark
@Composable
fun UserChatItemPreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {
        UserChatItem(userChat = UserChat(
            id = "1",
            members = mutableListOf(Member("1", "User1"), Member("2","User1234")),
            memberIds = mutableListOf("1", "NhZ187ifEKQSkk3Fkq5navKMBqG2"),
            lastMessageTime = System.currentTimeMillis(),
            lastMessageUserId = "1",
            lastMessageUsername = "User1",
            lastMessage = "Hello"
        ), navController = null)
    }
}