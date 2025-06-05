package com.example.chatterbox.chat.userChats.presentation

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatterbox.ChatScreenObject
import com.example.chatterbox.R
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.core.common.getRelativeTime
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.theme.ChatterBoxTheme

@Composable
fun UserItem(modifier: Modifier = Modifier, user: User, navController: NavController?) {

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
                .size(50.dp),
            showDot = false
        )

        Spacer(modifier.width(25.dp))

        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceAround
        ){
            Text(
                text = user.username,
                fontSize = 17.sp
            )
            Text(
                text = "last online: ${getRelativeTime(user.lastActive).toString()}"
            )

        }
    }
}

@Preview
@Composable
fun UserItemPreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {
        UserItem(
            user = User(
                id = "1",
                username = "User",
                email = "sample@gmailcom",
                description = "Descrption",
                profilePhotoUrl = "",
                status = "Online",
                lastActive = System.currentTimeMillis(),
                dateCreated = System.currentTimeMillis()
            ),
            navController = null
        )
    }
}