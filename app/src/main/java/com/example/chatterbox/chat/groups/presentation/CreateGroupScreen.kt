package com.example.chatterbox.chat.groups.presentation

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.R
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CreateGroupRoot(groupViewModel: GroupViewModel, navController: NavController, currentUsername: String, modifier: Modifier = Modifier) {
    val loadState = groupViewModel.loadState.collectAsStateWithLifecycle()
    CreateGroupScreen(loadState = loadState, modifier = modifier) { name, photoUrl, description ->
        groupViewModel.createGroup(
            currentUsername = currentUsername,
            name = name,
            photoUrl = photoUrl,
            description = description,
        ) { groupId ->
//            navController.navigate()
        }
    }

}

@Composable
fun CreateGroupScreen(modifier: Modifier = Modifier, loadState: State<LoadState>, createGroup: (String, String, String) -> Unit) {
    val TAG = "CreateGroupScreen"

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // TODO: Edit this after adding the functionality of uploading the profile photo
    var photoUrl by remember { mutableStateOf("") }
    val context = LocalContext.current


    Surface {
        if (loadState.value == LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {} // disables clicks
                    .zIndex(1f), // ensures it's on top
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            RoundImage(
                image = painterResource(R.drawable.google_logo),
                modifier = Modifier.size(125.dp),
                showDot = false
            )

            Spacer(Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Group name") },
                value = name,
                onValueChange = {
                    name = it
                }
            )

            Spacer(Modifier.height(25.dp))

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
                    if (name.trim().isEmpty() || description.trim().isEmpty()) {
                        Toast.makeText(context, "Please enter each value", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        createGroup(name, description, "")
                        Toast.makeText(context, "Group created", Toast.LENGTH_SHORT).show()
                    }

                }
            ) {
                Text("Create Group")
            }

            Spacer(Modifier.height(25.dp))

        }
    }


}

@PreviewLightDark
@Composable
fun CreateGroupScreenPreview(modifier: Modifier = Modifier) {
    val mockLoadState = remember { mutableStateOf(LoadState.Idle) }

    ChatterBoxTheme {
        CreateGroupScreen(
            loadState = mockLoadState,
        ) { name, photoUrl, description ->

        }
    }
}