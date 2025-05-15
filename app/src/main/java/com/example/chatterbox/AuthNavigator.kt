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
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.chat.users.presentation.EditProfileScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthNavigator(modifier: Modifier = Modifier) {
    val authViewModel = koinViewModel<AuthViewModel>()
    val user by authViewModel.user.collectAsState()
    val navController = rememberNavController()

    val startDestination = if (user != null) {
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
data class EditProfileScreenObject(
    val user: User
)