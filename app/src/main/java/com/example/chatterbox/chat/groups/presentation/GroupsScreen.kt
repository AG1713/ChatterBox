package com.example.chatterbox.chat.groups.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.CreateGroupRootObject
import com.example.chatterbox.SearchUsersRootObject
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.userChats.presentation.UserChatItem

@Composable
fun GroupsRoot(groupViewModel: GroupViewModel, currentUserId: String, navController: NavController, modifier: Modifier = Modifier) {
    val groups by groupViewModel.groups.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        groupViewModel.getAllGroups()
        onDispose {
            groupViewModel.clearListeners()
        }
    }

    GroupsScreen(groups = groups, currentUserId = currentUserId, navController = navController)

}

@Composable
fun GroupsScreen(groups: List<Group>, currentUserId: String, navController: NavController?, modifier: Modifier = Modifier) {
    val TAG = "ChatsScreen"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    navController?.navigate(CreateGroupRootObject)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.GroupAdd,
                    contentDescription = "Search Users"
                )
            }
        }
    ) { padding ->

        if (groups.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No groups found",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Log.d(TAG, "ChatsScreen: ${groups.size}")
                items(groups) { group ->
                    GroupItem(
                        group = group,
                        navController = navController,
                        currentUserId = currentUserId
                    )
                    if (group != groups[groups.lastIndex]) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}