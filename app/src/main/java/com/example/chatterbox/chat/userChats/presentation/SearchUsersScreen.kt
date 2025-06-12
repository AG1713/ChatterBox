package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.ChatScreenObject
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.presentation.UserViewModel
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchUsersRoot(userChatViewModel: UserChatViewModel, userViewModel: UserViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val users by userChatViewModel.users.collectAsStateWithLifecycle()
    val currentUser by userViewModel.user.collectAsStateWithLifecycle()

    if (currentUser != null) {
        SearchUsersScreen(users = users,
            currentUser = currentUser!!,
            navController = navController,
            modifier = modifier,
            callViewModel = { userChatViewModel.getAllUsersWithHint(it) },
            getOrCreateChat = { otherId, otherUsername, currentId, currentUsername ->
                userChatViewModel.getOrCreateUserChat(
                    otherId,
                    otherUsername,
                    currentId,
                    currentUsername,
                    onComplete = { chatRoomId ->
                        navController.navigate(
                            ChatScreenObject(
                                id = otherId,
                                username = otherUsername,
                                chatRoomId = chatRoomId
                            )
                        )
                    })
            })
    }
}

@Composable
fun SearchUsersScreen(
    users: List<User>,
    currentUser: User,
    navController: NavController?,
    modifier: Modifier = Modifier,
    callViewModel: (String) -> Unit,
    getOrCreateChat: (otherId: String, otherUsername: String, currentId: String, currentUsername: String) -> Unit) {
    val TAG = "SearchUsersScreen"

    var hint by remember {
        mutableStateOf("")
    }

    LaunchedEffect (hint) {
        Log.d(TAG, "SearchUsersScreen: LaunchedEffect triggered")
        delay(300) // Debouncing
        callViewModel(hint)
    }

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(15.dp),
        topBar = {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = hint,
                onValueChange = {
                    hint = it
                },
                label = {
                    Text("Username")
                },
                keyboardOptions = KeyboardOptions.Default
                    .copy(capitalization = KeyboardCapitalization.Sentences)
            )
        }
    ) { padding ->

        if (users.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No users found",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                items(users){ user ->
                    UserItem(user = user, navController = navController, onClick = {
                        getOrCreateChat(user.id, user.username, currentUser.id, currentUser.username)
                    })

                }
            }
        }

    }

}

@PreviewLightDark
@Composable
fun SearchUsersScreenPreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {
        SearchUsersScreen(
            users = listOf(
                User(
                    id = "u1",
                    username = "Alice",
                    email = "alice@example.com",
                    description = "Nature lover and tech enthusiast.",
                    profilePhotoUrl = "https://example.com/photos/alice.jpg",
                    status = "online",
                    lastActive = System.currentTimeMillis() - 5 * 60 * 1000, // 5 minutes ago
                    dateCreated = System.currentTimeMillis() - 100 * 24 * 60 * 60 * 1000L // 100 days ago
                ),
                User(
                    id = "u2",
                    username = "Bob",
                    email = "bob@example.com",
                    description = "Coffee addict and Android dev.",
                    profilePhotoUrl = "https://example.com/photos/bob.jpg",
                    status = "offline",
                    lastActive = System.currentTimeMillis() - 2 * 60 * 60 * 1000, // 2 hours ago
                    dateCreated = System.currentTimeMillis() - 200 * 24 * 60 * 60 * 1000L
                ),
                User(
                    id = "u3",
                    username = "Charlie",
                    email = "charlie@example.com",
                    description = "Loves gaming and music.",
                    profilePhotoUrl = "https://example.com/photos/charlie.jpg",
                    status = "idle",
                    lastActive = System.currentTimeMillis() - 15 * 60 * 1000, // 15 mins ago
                    dateCreated = System.currentTimeMillis() - 365 * 24 * 60 * 60 * 1000L // 1 year ago
                )
            ),
            currentUser = User(
                id = "u1",
                username = "Alice",
                email = "alice@example.com",
                description = "Nature lover and tech enthusiast.",
                profilePhotoUrl = "https://example.com/photos/alice.jpg",
                status = "online",
                lastActive = System.currentTimeMillis() - 5 * 60 * 1000, // 5 mins ago
                dateCreated = System.currentTimeMillis() - 100 * 24 * 60 * 60 * 1000L // 100 days ago
            ),
            navController = null,
            callViewModel = { },
            getOrCreateChat = {
                    otherUserId, otherUsername, currentUserId, currentUsername ->
            }
        )
    }
}