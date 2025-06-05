package com.example.chatterbox.chat.users.presentation

import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.text.format.DateUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.EditProfileRootObject
import com.example.chatterbox.R
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.core.common.getRelativeTime
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.theme.ChatterBoxTheme


@Composable
fun UserProfileRoot(userViewModel: UserViewModel, navController: NavController?, modifier: Modifier = Modifier) {
    val user by userViewModel.user.collectAsStateWithLifecycle()
    val loadState by userViewModel.loadState.collectAsStateWithLifecycle()
    UserProfileScreen(navController = navController, user = user, loadState = loadState)
}


@Composable
fun UserProfileScreen(
    navController: NavController?,
    modifier: Modifier = Modifier,
    user: User?,
    loadState: LoadState
) {
    val TAG = "UserProfileScreen"

    if (loadState == LoadState.Loading) {
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

    if (user != null) {
        Log.d(TAG, "UserProfileScreen: Not loading")
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier,
                    onClick = {
                        navController?.navigate(EditProfileRootObject)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
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
                    image = painterResource(R.drawable.google_logo),
                    showDot = (user.status == "Online"),
                    modifier = Modifier.size(125.dp)
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(25.dp))

                DescriptionCard(
                    "Member since",
                    getRelativeTime(user.dateCreated).toString()
                )

                Spacer(Modifier.height(15.dp))

                DescriptionCard(
                    "Bio",
                    user.description)


            }

        }

    }

}

@Composable
fun DescriptionCard(title: String, description: String, modifier: Modifier = Modifier) {

    Column (
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(15.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

@PreviewLightDark
@Composable
fun UserProfileScreenPreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {
        UserProfileScreen(
            navController = null,
            user = User(
                id = "1",
                username = "Username",
                email = "sample@gmail.com",
                description = "This is a description",
                profilePhotoUrl = "",
                status = "Online",
                lastActive = System.currentTimeMillis(),
                dateCreated = System.currentTimeMillis()
            ),
            loadState = LoadState.Idle
        )
    }


}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}