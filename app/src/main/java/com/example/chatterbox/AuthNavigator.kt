package com.example.chatterbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.chatterbox.auth.presentation.AuthViewModel
import com.example.chatterbox.auth.presentation.SignInScreen
import com.example.chatterbox.chat.ChatPagerScreen
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.groups.presentation.CreateGroupRoot
import com.example.chatterbox.chat.groups.presentation.GroupChatRoot
import com.example.chatterbox.chat.groups.presentation.GroupChatViewModel
import com.example.chatterbox.chat.groups.presentation.GroupViewModel
import com.example.chatterbox.chat.userChats.presentation.ChatRoot
import com.example.chatterbox.chat.userChats.presentation.ChatViewModel
import com.example.chatterbox.chat.userChats.presentation.SearchUsersRoot
import com.example.chatterbox.chat.userChats.presentation.UserChatViewModel
import com.example.chatterbox.chat.userChats.presentation.UserChatsRoot
import com.example.chatterbox.chat.users.presentation.EditProfileRoot
import com.example.chatterbox.chat.users.presentation.UserProfileRoot
import com.example.chatterbox.chat.users.presentation.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthNavigator(modifier: Modifier = Modifier) {
    val authViewModel:AuthViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()
    val userChatViewModel: UserChatViewModel = koinViewModel()
    val chatViewModel: ChatViewModel = koinViewModel()
    val groupViewModel: GroupViewModel = koinViewModel()
    val groupChatViewModel: GroupChatViewModel = koinViewModel()
    val navController = rememberNavController()

    val startDestination = if (FirebaseAuth.getInstance().currentUser?.uid != null) {
        ChatPagerScreenObject
    } else {
        SignInScreenObject
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable<SignInScreenObject>{
            SignInScreen(authViewModel = authViewModel, navController = navController)
        }
        composable<ChatPagerScreenObject>{
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

            ChatPagerScreen(
                userViewModel = userViewModel,
                userChatViewModel = userChatViewModel,
                groupViewModel = groupViewModel,
                navController = navController,
                currentUserId = currentUserId
            )
        }
        composable<UserChatsRootObject>{
            UserChatsRoot(userChatViewModel = userChatViewModel, navController = navController)
        }
        composable<ChatScreenObject>{
            val args = it.toRoute<ChatScreenObject>()
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val currentUsername = userViewModel.user.value?.username
            ChatRoot(
                chatViewModel = chatViewModel,
                chatRoomId = args.chatRoomId,
                otherUserId = args.id,
                otherUsername = args.username,
                currentUserId = currentUserId,
                currentUsername = currentUsername!!
            )
        }
        composable<UserProfileRootObject>{
            UserProfileRoot(userViewModel = userViewModel, navController = navController)
        }
        composable<SearchUsersRootObject>{
            SearchUsersRoot(userChatViewModel = userChatViewModel, userViewModel = userViewModel, navController = navController)
        }
        composable<EditProfileRootObject> {
            EditProfileRoot(userViewModel = userViewModel, navController = navController)
        }
        composable<CreateGroupRootObject> {
            val currentUsername = userViewModel.user.value?.username
            CreateGroupRoot(groupViewModel = groupViewModel, navController = navController, currentUsername = currentUsername!!)
        }
        composable<GroupChatRootObject> {
            val args = it.toRoute<GroupChatRootObject>()
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val currentUsername = userViewModel.user.value?.username

            GroupChatRoot(
                groupChatViewModel = groupChatViewModel,
                groupId = args.groupId,
                groupName = args.groupName,
                currentUserId = currentUserId,
                currentUsername = currentUsername!!
            )
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
data class ChatScreenObject(
    val chatRoomId: String,
    val id: String,
    val username: String
)

@Serializable
object EditProfileRootObject

@Serializable
object SearchUsersRootObject

@Serializable
object CreateGroupRootObject

@Serializable
data class GroupChatRootObject(
    val groupId: String,
    val groupName: String
)