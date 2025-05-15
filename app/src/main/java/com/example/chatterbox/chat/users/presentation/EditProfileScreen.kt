package com.example.chatterbox.chat.users.presentation

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chatterbox.R
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProfileScreen(user1: User, navController: NavController?, modifier: Modifier = Modifier) {

    val TAG = "EditProfileScreen"
    val activityOwner = LocalContext.current.findActivity()?.viewModelStore
    // Force scoping of the ViewModel to the Activity
//    val userViewModel: UserViewModel = koinViewModel(
//        viewModelStoreOwner = object : ViewModelStoreOwner {
//            override val viewModelStore = activityOwner!!
//        }
//    )
    val userViewModel = koinViewModel<UserViewModel>()
    val user by userViewModel.user.collectAsState()
    val loadState by userViewModel.loadState.collectAsStateWithLifecycle()

    var username by remember { mutableStateOf(user.user!!.username) }
    var description by remember { mutableStateOf(user.user!!.description) }
    // TODO: Edit this after adding the functionality of uploading the profile photo
    var profilePhotoUrl by remember { mutableStateOf(user.user?.profilePhotoUrl) }

    LaunchedEffect(loadState) {
        if (loadState == LoadState.Success) {
            navController?.popBackStack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 50.dp,
                start = 15.dp,
                end = 15.dp,
                bottom = 15.dp
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loadState == LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // faded background
                    .clickable(enabled = false) {} // disables clicks
                    .zIndex(1f), // ensures it's on top
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            RoundImage(
                image = painterResource(R.drawable.google_logo),
                modifier = Modifier.size(125.dp)
            )

            Spacer(Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Username") },
                value = username,
                onValueChange = {
                    username = it
                }
            )

            Spacer(Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Description") },
                value = description,
                onValueChange = {
                    description = it
                }
            )

            Spacer(Modifier.height(25.dp))

            Button(
                onClick = {
                    Log.d(TAG, "Save changes clicked")
                    val newUser = user.user!!.copy(username = username, description = description)
                    userViewModel.updateCurrentUser(newUser)
                    Log.d(TAG, "Save changes onClick ended")
                }
            ) {
                Text("Save changes")
            }
        }

    }


}

@PreviewLightDark
@Composable
fun EditProfileScreenPreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {
        EditProfileScreen(
            User(
                id = "1",
                username = "Username",
                email = "sample@gmail.com",
                description = "This is a description",
                profilePhotoUrl = "",
                status = "Online",
                lastActive = System.currentTimeMillis(),
                dateCreated = System.currentTimeMillis()
            ),
            null
        )
    }
}