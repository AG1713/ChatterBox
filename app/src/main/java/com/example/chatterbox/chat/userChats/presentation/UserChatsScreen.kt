package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.SearchUsersRootObject
import com.example.chatterbox.chat.userChats.domain.Member
import com.example.chatterbox.chat.userChats.domain.UserChat
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserChatsRoot(userChatViewModel: UserChatViewModel, navController: NavController?, modifier: Modifier = Modifier) {
    val userChats by userChatViewModel.userChats.collectAsStateWithLifecycle()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    DisposableEffect(Unit) {
        userChatViewModel.getAllUserChats()

        onDispose {
            userChatViewModel.clearUserChatsListeners()
        }
    }

    UserChatsScreen(currentUserId = currentUserId, userChats = userChats, navController = navController)
}

@Composable
fun UserChatsScreen(currentUserId: String?, userChats: List<UserChat>, navController: NavController?) {
    val TAG = "ChatsScreen"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController?.navigate(SearchUsersRootObject) }
            ) {
                Icon(
                    imageVector = Icons.Default.AddComment,
                    contentDescription = "Search Users"
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Log.d(TAG, "ChatsScreen: ${userChats.size}")
            items(userChats) {
                UserChatItem(currentUserId = currentUserId, userChat = it, navController = navController)
                if (it != userChats[userChats.lastIndex])
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Gray
                    )
            }
        }
    }

}

@PreviewLightDark
@Composable
fun UserChatsScreenPreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {
        UserChatsScreen(
            userChats = listOf(
                UserChat(
                    id = "chat1",
                    members = mutableListOf(
                        Member(id = "1", username = "Alice"),
                        Member(id = "u2", username = "Bob")
                    ),
                    memberIds = mutableListOf("1", "u2"),
                    lastMessageTime = 1720160000000,
                    lastMessageUserId = "1",
                    lastMessageUsername = "Alice",
                    lastMessage = "Hey Bob, how are you?"
                ),
                UserChat(
                    id = "chat2",
                    members = mutableListOf(
                        Member(id = "u3", username = "Charlie"),
                        Member(id = "u4", username = "David")
                    ),
                    memberIds = mutableListOf("u3", "u4"),
                    lastMessageTime = 1720155000000,
                    lastMessageUserId = "u4",
                    lastMessageUsername = "David",
                    lastMessage = "Let’s meet tomorrow."
                ),
                UserChat(
                    id = "chat3",
                    members = mutableListOf(
                        Member(id = "u5", username = "Eve"),
                        Member(id = "u6", username = "Frank")
                    ),
                    memberIds = mutableListOf("u5", "u6"),
                    lastMessageTime = 1720150000000,
                    lastMessageUserId = "u5",
                    lastMessageUsername = "Eve",
                    lastMessage = "Sure, I’ll send the document."
                )
            ),
            currentUserId = "1",
            navController = null
        )
    }
}