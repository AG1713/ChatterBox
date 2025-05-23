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
import com.example.chatterbox.chat.userChats.presentation.UserChatsScreen
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.presentation.EditProfileScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthNavigator(modifier: Modifier = Modifier) {
    val authViewModel = koinViewModel<AuthViewModel>()
    val user by authViewModel.user.collectAsState()
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
            SignInScreen(navController)
        }
        composable<ChatPagerScreenObject>{
            ChatPagerScreen(navController)
        }
        composable<ChatScreenObject>{
            ChatScreen()
        }
        composable (
            // toRoute<T>() only needs T to match the route you're currently in — not the caller.
            route = "edit_profile_screen/{user}",
            arguments = listOf(
                navArgument("user") {
                    type = NavType.StringType
                    nullable = false // Not mandatory
                }
            )
        ){
            entry: NavBackStackEntry ->
            val userJson = entry.arguments?.getString("user")
            val oldUser = Json.decodeFromString<User>(Uri.decode(userJson))

            EditProfileScreen(user1 = oldUser, navController)

        }



    }

}

@Serializable
object SignInScreenObject

@Serializable
object ChatPagerScreenObject

@Serializable
object ChatScreenObject

@Serializable
data class EditProfileScreenObject(
    val user: User
)