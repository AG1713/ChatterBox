package com.example.chatterbox.chat.userChats.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.ChatPagerScreenObject
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.presentation.LoadState
import com.example.chatterbox.chat.users.presentation.UserViewModel
import com.example.chatterbox.core.common.getRelativeTime
import com.example.chatterbox.ui.components.DescriptionCard
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.theme.ChatterBoxTheme

@Composable
fun UserInfoRoot(userViewModel: UserViewModel, userChatViewModel: UserChatViewModel, userChatId: String, userId: String, navController: NavController, modifier: Modifier = Modifier) {
    val user = userViewModel.otherUser.collectAsStateWithLifecycle()
    val loadState = userViewModel.loadState.collectAsStateWithLifecycle()
    val deleteLoadState = userChatViewModel.loadState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        userViewModel.getUser(userId)
        onDispose {
            userViewModel.clearOtherUser()
        }
    }

    UserInfoScreen(
        user = user,
        loadState = loadState,
        deleteLoadState = deleteLoadState,
        navController = navController,
        modifier = modifier,
        deleteChat = {
            userChatViewModel.deleteUserChat(
                userChatId = userChatId,
                onComplete = {
                    navController.popBackStack(ChatPagerScreenObject, inclusive = false)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(user: State<User?>, loadState: State<LoadState>,
                   deleteLoadState: State<com.example.chatterbox.chat.shared.domain.LoadState>,
                   navController: NavController?, modifier: Modifier = Modifier,
                   deleteChat: () -> Unit) {
    val TAG = "UserProfileScreen"
    var expanded by remember { mutableStateOf(false) }

    if (loadState.value == LoadState.Loading || deleteLoadState.value == com.example.chatterbox.chat.shared.domain.LoadState.Loading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // faded background
                .clickable(enabled = false) {} // disables clicks
                .zIndex(1f), // ensures it's on top
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        Log.d(TAG, "UserProfileScreen: Loading")
    }

    if (user.value != null) {
        Log.d(TAG, "UserProfileScreen: Not loading")
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {},
                    actions = {
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    deleteChat()
                                },
                                text = {
                                    Text(
                                        text = "Delete chat"
                                    )
                                }
                            )
                        }

                    }

                )
            },
        ) {
                padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        padding
                    )
                    .padding(
                        top = 50.dp,
                        start = 15.dp,
                        end = 15.dp,
                        bottom = 15.dp
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val online = ((System.currentTimeMillis() - user.value!!.lastActive) < 2_40_000)
                RoundImage(
                    model = user.value!!.profilePhotoUrl,
                    showDot = online,
                    modifier = Modifier.size(125.dp)
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    text = user.value!!.username,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = user.value!!.email,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(25.dp))

                DescriptionCard(
                    "Member since",
                    getRelativeTime(user.value!!.dateCreated).toString()
                )

                Spacer(Modifier.height(15.dp))

                DescriptionCard(
                    "Bio",
                    user.value!!.description
                )

                Spacer(Modifier.height(15.dp))

                if (!online){
                    DescriptionCard(
                        "Last online",
                        getRelativeTime(user.value!!.lastActive).toString()
                    )
                }

            }

        }

    }

}

@PreviewLightDark
@Composable
fun UserInfoScreenPreview(modifier: Modifier = Modifier) {
    val sampleUser = remember {
        mutableStateOf(
            User(
                id = "123",
                username = "Avdhoot",
                email = "avdhoot@example.com",
                description = "Engineering student & coder",
                profilePhotoUrl = "https://example.com/photo.jpg",
                lastActive = System.currentTimeMillis(),
                dateCreated = System.currentTimeMillis() - 1000000,
                messageToken = "dummy_token_abc123"
            )
        )
    }

    val loadState = remember { mutableStateOf(LoadState.Success) }
    val deleteState = remember { mutableStateOf(com.example.chatterbox.chat.shared.domain.LoadState.Idle) }

    ChatterBoxTheme {
        UserInfoScreen(
            user = sampleUser,
            loadState = loadState,
            deleteLoadState = deleteState,
            navController = null,
            deleteChat = {}
        )
    }
}

