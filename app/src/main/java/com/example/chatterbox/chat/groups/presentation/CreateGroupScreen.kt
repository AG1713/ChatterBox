package com.example.chatterbox.chat.groups.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.CreateGroupRootObject
import com.example.chatterbox.GroupChatRootObject
import com.example.chatterbox.R
import com.example.chatterbox.core.common.convertToJpeg
import com.example.chatterbox.core.common.maxCharsForDescription
import com.example.chatterbox.core.common.maxCharsForUsername
import com.example.chatterbox.core.common.maxLinesForDescription
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CreateGroupRoot(groupViewModel: GroupViewModel, navController: NavController, currentUsername: String, modifier: Modifier = Modifier) {
    val loadState = groupViewModel.loadState.collectAsStateWithLifecycle()
    CreateGroupScreen(loadState = loadState, modifier = modifier) { name, description, context, uri ->
        groupViewModel.createGroup(
            currentUsername = currentUsername,
            name = name,
            description = description,
            convertFunction = { convertToJpeg(context = context, uri = uri) },
        ) { groupId, groupName ->
            navController.navigate(GroupChatRootObject(groupId, groupName)) {
                popUpTo<CreateGroupRootObject>() { inclusive = true }
            }
        }
    }

}

@Composable
fun CreateGroupScreen(modifier: Modifier = Modifier, loadState: State<LoadState>, createGroup: (String, String, Context, Uri?) -> Unit) {
    val TAG = "CreateGroupScreen"

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var newUri by remember { mutableStateOf<Uri?>(null) }
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            newUri = uri
        }
    }


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
            
            Row {
                RoundImage(
                    model = newUri,
                    modifier = Modifier.size(125.dp).clickable { dropDownMenuExpanded = true },
                    editable = true
                )
                DropdownMenu(
                    expanded = dropDownMenuExpanded,
                    onDismissRequest = { dropDownMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Upload") },
                        onClick = {
                            dropDownMenuExpanded = false
                            launcher.launch(arrayOf("image/jpeg", "image/png"))
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Remove") },
                        onClick = {
                            dropDownMenuExpanded = false
                            newUri = null
                        }
                    )
                }

            }


            Spacer(Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Group name") },
                value = name,
                onValueChange = {
                    if (it.length <= maxCharsForUsername) {
                        name = it
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
                    .copy(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(Modifier.height(25.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Description") },
                value = description,
                onValueChange = {
                    if ((it.length <= maxCharsForDescription && it.count { ch -> ch == '\n' } <= maxLinesForDescription)) {
                        description = it
                    }
                    Log.d(TAG, "CreateGroupScreen: $description")
                },
                keyboardOptions = KeyboardOptions.Default
                    .copy(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(Modifier.height(25.dp))

            Button(
                onClick = {
                    if (name.trim().isEmpty() || description.trim().isEmpty()) {
                        Toast.makeText(context, "Please enter each value", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        createGroup(name, description, context, newUri)
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
        ) { name, description, context, uri ->

        }
    }
}