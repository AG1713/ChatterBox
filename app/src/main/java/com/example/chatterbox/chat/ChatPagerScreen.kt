package com.example.chatterbox.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.chatterbox.SignInScreenObject
import com.example.chatterbox.chat.groups.presentation.GroupViewModel
import com.example.chatterbox.chat.groups.presentation.GroupsRoot
import com.example.chatterbox.chat.userChats.presentation.UserChatViewModel
import com.example.chatterbox.chat.userChats.presentation.UserChatsRoot
import com.example.chatterbox.chat.users.presentation.UserProfileRoot
import com.example.chatterbox.chat.users.presentation.UserViewModel
import com.example.chatterbox.core.common.ListenerRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun ChatPagerScreen (
    userViewModel: UserViewModel,
    userChatViewModel: UserChatViewModel,
    groupViewModel: GroupViewModel,
    currentUserId: String,
    navController: NavController?,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        userViewModel.getCurrentUser()
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .update("messageToken", token)
                }
            }
    }

    val context = LocalContext.current
    DisposableEffect(Unit) {
        onDispose {
            context.imageLoader.diskCache?.clear()
            context.imageLoader.memoryCache?.clear()
        }
    }

    val scope = rememberCoroutineScope()

    val tabs = listOf(
            TabItem(
                title = "Home",
                unselectedIcon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,

            ) {
                UserChatsRoot(userChatViewModel = userChatViewModel, navController = navController)
            },
            TabItem(
                title = "Groups",
                unselectedIcon = Icons.Outlined.Groups,
                selectedIcon = Icons.Filled.Groups,
            ) {
                GroupsRoot(
                    groupViewModel = groupViewModel,
                    navController = navController!!,
                    currentUserId = currentUserId
                )
            },
            TabItem(
                title = "Profile",
                unselectedIcon = Icons.Outlined.Person,
                selectedIcon = Icons.Filled.Person,
            ) {
                UserProfileRoot(userViewModel = userViewModel, navController = navController)
            },
        )

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState {
        tabs.size
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Scaffold (
        modifier = modifier,
        topBar = {
            var expanded by remember { mutableStateOf(false) }

            Column {

                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    title = {
                        Text(
                            text = "ChatterBox",
                            fontSize = 25.sp
                        )
                    },
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
                                    ListenerRegistry.clearAll()
                                    Log.d("ListenerRegistry", "ChatPagerScreen")
                                    navController?.navigate(SignInScreenObject) {
                                        popUpTo(0) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                          },
                                text = {
                                            Text(
                                                text = "LogOut"
                                            )
                                       }
                            )
                        }

                    }

                )
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(5.dp), // adjust thickness
                            color = MaterialTheme.colorScheme.onPrimaryContainer // or any strong contrast color
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, tabItem ->
                        Tab(
                            selected = (index == selectedTabIndex),
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                                selectedTabIndex = index
                            },
                            text = {
                                Text(text = tabItem.title)
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedTabIndex) tabItem.selectedIcon else tabItem.unselectedIcon,
                                    contentDescription = tabItem.title
                                )
                            }
                        )
                    }
                }

            }


        }

    ){ padding ->

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),

        ) { index ->
            tabs[index].composable()
        }

    }

}

@Composable
fun Sample(modifier: Modifier = Modifier.background(Color.Gray)) {
    Text(text = "Page")
}

data class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val composable: @Composable () -> Unit
)

@PreviewLightDark
@Composable
fun ChatPagerScreenPreview(modifier: Modifier = Modifier) {
//    ChatterBoxTheme {
//        ChatPagerScreen(
//            navController = null,
//            userChatViewModel = koinViewModel(),
//            userViewModel = koinViewModel()
//        )
//    }
}