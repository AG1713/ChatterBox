package com.example.chatterbox.chat.users.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatterbox.chat.users.domain.ChatRoom
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import com.google.firebase.Timestamp

@Composable
fun ChatsScreen(
    modifier: Modifier = Modifier,
) {

    val chats: List<ChatRoom> = listOf(
        ChatRoom(
            id = "1",
            type = "personal",
            groupName = "",
            groupDescription = "",
            groupPhotoUrl = "none",
            lastMessageSentTime = Timestamp.now(),
            lastMessageSentUserId = "1",
            lastMessageSentUsername = "user1",
            lastMessageSent = "Hello"
        ),
        ChatRoom(
            id = "2",
            type = "personal",
            groupName = "",
            groupDescription = "",
            groupPhotoUrl = "none",
            lastMessageSentTime = Timestamp.now(),
            lastMessageSentUserId = "2",
            lastMessageSentUsername = "user2",
            lastMessageSent = "Go away"
        ),
        ChatRoom(
            id = "3",
            type = "personal",
            groupName = "",
            groupDescription = "",
            groupPhotoUrl = "none",
            lastMessageSentTime = Timestamp.now(),
            lastMessageSentUserId = "1",
            lastMessageSentUsername = "user1",
            lastMessageSent = "Hello"
        ),
    )

    ChatterBoxTheme {

        Row (modifier = Modifier.padding(5.dp)){
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Text"
            )
            Spacer(Modifier.width(5.dp))



            Column {

            }

        }

    }
}