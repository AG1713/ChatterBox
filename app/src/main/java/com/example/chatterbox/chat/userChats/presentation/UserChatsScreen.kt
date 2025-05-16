package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserChatsScreen(navController: NavController) {
    val TAG = "ChatsScreen"
    val userChatViewModel = koinViewModel<UserChatViewModel>()
    val userChats by userChatViewModel.userChats.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp)
    ) {
        Log.d(TAG, "ChatsScreen: ${userChats.size}")
        items(userChats) {
            UserChatItem(userChat = it, navController = navController)
        }
    }
}

