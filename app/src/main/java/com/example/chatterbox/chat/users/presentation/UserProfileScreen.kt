package com.example.chatterbox.chat.users.presentation

import android.net.Uri
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.EditProfileScreenObject
import com.example.chatterbox.R
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.ui.components.RoundImage
import com.example.compose.ChatterBoxTheme
import com.google.firebase.Timestamp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProfileScreen(
    navController: NavController?,
    modifier: Modifier = Modifier
) {
    val TAG = "UserProfileScreen"


    val userViewModel = koinViewModel<UserViewModel>()
    val userState by userViewModel.user.collectAsStateWithLifecycle()

    if (userState.isLoading) {
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

    if (userState.user != null) {
        Log.d(TAG, "UserProfileScreen: Not loading")
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier,
                    onClick = {
                        val userJson = Json.encodeToString(userState.user)
                        val encodedUserJson = Uri.encode(userJson)
                        navController?.navigate("edit_profile_screen/$encodedUserJson")
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
                    modifier = Modifier.size(125.dp),
                    image = painterResource(R.drawable.google_logo)
                )
                Spacer(Modifier.height(15.dp))

                Text(
                    text = userState.user!!.username,
                    fontSize = 35.sp
                )
                Text(
                    text = userState.user!!.email,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DescriptionCard(
                        userState.user!!.status,
                        "status",
                        Modifier.weight(1f))
                    Spacer(Modifier.width(5.dp))
                    DescriptionCard(
                        DateUtils.getRelativeTimeSpanString(
                            userState.user!!.lastActive
                        ).toString(),
                        "Last active",
                        Modifier.weight(1f))
                    Spacer(Modifier.width(5.dp))
                    DescriptionCard(
                        DateUtils.getRelativeTimeSpanString(
                            userState.user!!.dateCreated
                        ).toString(),
                        "created on",
                        Modifier.weight(1f))
                }
                Spacer(Modifier.height(25.dp))
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Bio",
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth(),
                    text = userState.user!!.description,
                )

            }

        }


    }

}

@Composable
fun DescriptionCard(title: String, description: String, modifier: Modifier = Modifier) {

    Column (
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .padding(horizontal = 15.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 15.sp
        )
        Text(
            text = description,
            fontSize = 12.sp
        )
    }

}

@PreviewLightDark
@Composable
fun UserProfileScreenPreview(modifier: Modifier = Modifier) {
//    ChatterBoxTheme {
//        UserProfileScreen(
//            null,
//            UserState(
//                false,
//                User(
//                    id = "1",
//                    username = "Username",
//                    email = "sample@gmail.com",
//                    description = "This is a description",
//                    profilePhotoUrl = "",
//                    status = "Online",
//                    lastActive = System.currentTimeMillis(),
//                    dateCreated = System.currentTimeMillis()
//                )
//            )
//        )
//    }


}