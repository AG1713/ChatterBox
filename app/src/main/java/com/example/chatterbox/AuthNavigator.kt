package com.example.chatterbox

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatterbox.auth.presentation.AuthViewModel
import com.example.chatterbox.auth.presentation.SignInScreen
import com.example.chatterbox.chat.ChatPagerScreen
import com.example.chatterbox.chat.userChats.presentation.ChatScreen
import com.example.chatterbox.chat.userChats.presentation.SearchUsersRoot
import com.example.chatterbox.chat.userChats.presentation.SearchUsersScreen
import com.example.chatterbox.chat.userChats.presentation.UserChatViewModel
import com.example.chatterbox.chat.userChats.presentation.UserChatsRoot
import com.example.chatterbox.chat.userChats.presentation.UserChatsScreen
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.presentation.EditProfileRoot
import com.example.chatterbox.chat.users.presentation.EditProfileScreen
import com.example.chatterbox.chat.users.presentation.UserProfileRoot
import com.example.chatterbox.chat.users.presentation.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthNavigator(modifier: Modifier = Modifier) {
    val authViewModel:AuthViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()
    val userChatViewModel: UserChatViewModel = koinViewModel()
    val navController = rememberNavController()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    val startDestination = if (currentUserId != null) {
        ChatPagerScreenObject
    } else {
        SignInScreenObject
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable<SignInScreenObject>{
            SignInScreen(navController = navController)
        }
        composable<ChatPagerScreenObject>{
            ChatPagerScreen(userViewModel = userViewModel, userChatViewModel = userChatViewModel, navController = navController)
        }
        composable<UserChatsRootObject>{
            UserChatsRoot(userChatViewModel = userChatViewModel, navController = navController)
        }
        composable<ChatScreenObject>{
            ChatScreen()
        }
        composable<UserProfileRootObject>{
            UserProfileRoot(userViewModel = userViewModel, navController = navController)
        }
        composable<SearchUsersRootObject>{
            SearchUsersRoot(userChatViewModel = userChatViewModel, navController = navController)
        }
        composable<EditProfileRootObject> {
            EditProfileRoot(userViewModel = userViewModel, navController = navController)
        }

    }

}

@Serializable
object SignInScreenObject

@Serializable
object ChatPagerScreenObject

@Serializable
object UserChatsRootObject

@Serializable
object UserProfileRootObject

@Serializable
object ChatScreenObject

@Serializable
object EditProfileRootObject

@Serializable
object SearchUsersRootObject