package com.example.chatterbox.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.chat.users.presentation.UserProfileScreen
import com.example.chatterbox.chat.users.presentation.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatPagerScreen(navController: NavController, modifier: Modifier = Modifier) {

    val scope = rememberCoroutineScope()
    val userViewModel: UserViewModel = koinViewModel()
    val userState = userViewModel.user.collectAsStateWithLifecycle()

    val tabs = listOf(
            TabItem(
                title = "Home",
                unselectedIcon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,
            ) {
                Sample()
            },
            TabItem(
                title = "Groups",
                unselectedIcon = Icons.Outlined.Groups,
                selectedIcon = Icons.Filled.Groups,
            ) {
                Sample()
            },
            TabItem(
                title = "Profile",
                unselectedIcon = Icons.Outlined.Person,
                selectedIcon = Icons.Filled.Person,
            ) {
                UserProfileScreen(navController)
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
        topBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
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