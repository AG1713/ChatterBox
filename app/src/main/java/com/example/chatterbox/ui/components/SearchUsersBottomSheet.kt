package com.example.chatterbox.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatterbox.chat.userChats.presentation.UserItem
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUsersBottomSheet(
    isVisible: Boolean,
    users: State<List<User>>,
    searchUsers: (String) -> Unit,
    onClick: (user: User) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val TAG = "SearchUsersBottomSheet"

    var hint by remember {
        mutableStateOf("")
    }

    LaunchedEffect (hint) {
        Log.d(TAG, "SearchUsersScreen: LaunchedEffect triggered")
        delay(300) // Debouncing
        searchUsers(hint)
    }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(isVisible) {
        if (isVisible) sheetState.show()
        else sheetState.hide()
    }

    if (isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    onDismissRequest()
                }
            }
        ) {
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

                if (users.value.isEmpty()) {
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
                        items(users.value) { user ->
                            UserItem(user = user, onClick = {
                                onClick(user)
                            })

                        }
                    }
                }

            }
        }
    }
}

@PreviewLightDark
@Composable
fun SearchUsersBottomSheetPreview(modifier: Modifier = Modifier) {
    val sampleUsers = remember {
        mutableStateOf(
            listOf(
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
            )
        )
    }

    ChatterBoxTheme {
        SearchUsersBottomSheet(
            isVisible = true,
            users = sampleUsers,
            searchUsers = {},
            onClick = {},
            onDismissRequest = {}
        )
    }
}