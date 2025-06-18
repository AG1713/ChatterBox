package com.example.chatterbox.chat.groups.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.BuildConfig
import com.example.chatterbox.ChatPagerScreenObject
import com.example.chatterbox.EditGroupRootObject
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.shared.domain.Member
import com.example.chatterbox.ui.components.SearchUsersBottomSheet
import com.example.chatterbox.chat.userChats.presentation.UserChatViewModel
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.presentation.UserViewModel
import com.example.chatterbox.core.common.SupabaseBuckets
import com.example.chatterbox.core.common.getRelativeTime
import com.example.chatterbox.ui.components.DescriptionCard
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.theme.ChatterBoxTheme

@Composable
fun GroupInfoRoot(groupId: String, groupViewModel: GroupViewModel, userViewModel: UserViewModel, userChatViewModel: UserChatViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val currentGroup by groupViewModel.currentGroup.collectAsStateWithLifecycle()
    val loadState = groupViewModel.loadState.collectAsStateWithLifecycle()
    val currentUser by userViewModel.user.collectAsStateWithLifecycle()
    val users = userChatViewModel.users.collectAsStateWithLifecycle()

    groupViewModel.getGroup(groupId)

    GroupInfoScreen(
        group = currentGroup,
        loadState = loadState,
        users = users,
        navController = navController,
        searchUsers = { hint ->
            userChatViewModel.getAllUsersWithHint(hint)
        },
        addMembers = { memberIds, members ->
            groupViewModel.addMembers(currentGroup!!.id, memberIds = memberIds, members = members)
            groupViewModel.getGroup(groupId)
        },
        leaveGroup = {
            groupViewModel.leaveGroup(groupId = currentGroup!!.id, userId = currentUser!!.id, username = currentUser!!.username) {
                navController.popBackStack(ChatPagerScreenObject, inclusive = false)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInfoScreen(group: Group?, loadState: State<LoadState>, users: State<List<User>>,
                    navController: NavController?,
                    modifier: Modifier = Modifier,
                    addMembers: (List<String>, List<Member>) -> Unit, leaveGroup: () -> Unit,
                    searchUsers: (String) -> Unit) {
    val TAG = "UserProfileScreen"
    var expanded by remember { mutableStateOf(false) }
    var isSheetVisible by remember { mutableStateOf(false) }

    if (loadState.value == LoadState.Loading) {
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
    }

    if (group != null) {
        SearchUsersBottomSheet(
            isVisible = isSheetVisible,
            users = users,
            searchUsers = searchUsers,
            onClick = { user ->
                addMembers(listOf(user.id), listOf(Member(user.id, user.username)))
                isSheetVisible = false
            },
            onDismissRequest = {
                isSheetVisible = false
            }
        )

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
                                    navController?.navigate(EditGroupRootObject(groupId = group.id, groupName = group.name))
                                },
                                text = {
                                    Text(
                                        text = "Edit group"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    leaveGroup()
                                },
                                text = {
                                    Text(
                                        text = "Leave group"
                                    )
                                }
                            )
                        }

                    }

                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier,
                    onClick = {
                        isSheetVisible = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Edit fab"
                    )
                }
            }
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

                RoundImage(
                    model = group.groupPhotoUrl,
                    modifier = Modifier.size(125.dp)
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(25.dp))

                DescriptionCard(
                    "Created",
                    getRelativeTime(group.creationTimestamp).toString()
                )

                Spacer(Modifier.height(15.dp))

                DescriptionCard(
                    "Description",
                    group.description
                )

                Spacer(Modifier.height(15.dp))

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(15.dp),
                    columns = GridCells.Fixed(3),
                ) {
                    item {
                        Text(
                            text = "Members",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Spacer(Modifier.height(35.dp))
                    }
                    item {  }
                    item {  }
                    items(group.members){ member ->
                        MemberDisplay(
                            member = member
                        )
                    }
                }


            }

        }

    }
}


@Composable
fun MemberDisplay(member: Member, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RoundImage(
            model = "${BuildConfig.SUPABASE_URL}storage/v1/object/public/${SupabaseBuckets.USER_PHOTOS}/${member.id}.jpg",
            showDot = false,
            modifier = Modifier.size(75.dp)
        )
        Box(
            modifier = Modifier
                .offset(y = (-10).dp)
                .zIndex(1f) // makes it appear on top
                .background(Color.White, shape = CircleShape)
                .padding(5.dp),
        ) {
            Text(
                text = member.username,
                color = MaterialTheme.colorScheme.background
            )
        }
    }

}

@PreviewLightDark
@Composable
fun GroupInfoScreenPreview(modifier: Modifier = Modifier) {
    val mockLoadState = remember { mutableStateOf(LoadState.Idle) }
    val sampleUsers = remember {
        mutableStateOf(
            listOf(
                User(
                    id = "u1",
                    username = "Alice",
                    email = "alice@example.com",
                    description = "Nature lover and tech enthusiast.",
                    profilePhotoUrl = "https://example.com/photos/alice.jpg",
                    lastActive = System.currentTimeMillis() - 5 * 60 * 1000, // 5 minutes ago
                    dateCreated = System.currentTimeMillis() - 100 * 24 * 60 * 60 * 1000L // 100 days ago
                ),
                User(
                    id = "u2",
                    username = "Bob",
                    email = "bob@example.com",
                    description = "Coffee addict and Android dev.",
                    profilePhotoUrl = "https://example.com/photos/bob.jpg",
                    lastActive = System.currentTimeMillis() - 2 * 60 * 60 * 1000, // 2 hours ago
                    dateCreated = System.currentTimeMillis() - 200 * 24 * 60 * 60 * 1000L
                ),
                User(
                    id = "u3",
                    username = "Charlie",
                    email = "charlie@example.com",
                    description = "Loves gaming and music.",
                    profilePhotoUrl = "https://example.com/photos/charlie.jpg",
                    lastActive = System.currentTimeMillis() - 15 * 60 * 1000, // 15 mins ago
                    dateCreated = System.currentTimeMillis() - 365 * 24 * 60 * 60 * 1000L // 1 year ago
                )
            )
        )
    }

    ChatterBoxTheme {
        GroupInfoScreen(
            group = Group(
                id = "g1",
                name = "Group",
                groupPhotoUrl = "",
                description = "This is group",
                creationTimestamp = System.currentTimeMillis(),
                memberIds = listOf("u1", "u2", "u3"),
                members = mutableListOf(Member("u1", "User1"), Member("u2","User1234"), Member("u3","New User"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234"), Member("u2","User1234")),
            ),
            users = sampleUsers,
            loadState = mockLoadState,
            navController = null,
            searchUsers = {},
            addMembers = { memberids, members -> },
            leaveGroup = {}
        )
    }
}