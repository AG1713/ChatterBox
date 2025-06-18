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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.chatterbox.App
import com.example.chatterbox.EditGroupRootObject
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.shared.domain.Member
import com.example.chatterbox.chat.users.domain.User
import com.example.chatterbox.core.common.convertToJpeg
import com.example.chatterbox.core.common.getRelativeTime
import com.example.chatterbox.core.common.maxCharsForDescription
import com.example.chatterbox.core.common.maxCharsForUsername
import com.example.chatterbox.core.common.maxLinesForDescription
import com.example.chatterbox.ui.components.DescriptionCard
import com.example.chatterbox.ui.components.RoundImage
import com.example.chatterbox.ui.components.SearchUsersBottomSheet
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditGroupRoot(groupId: String, groupName: String, groupViewModel: GroupViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val group = groupViewModel.currentGroup.collectAsStateWithLifecycle()
    val loadState = groupViewModel.loadState.collectAsStateWithLifecycle()

    groupViewModel.getGroup(groupId)

    EditGroupScreen(
        group = group,
        loadState = loadState,
        updateGroup = { newGroup: Group, uri: Uri?, context: Context ->
            CoroutineScope(Dispatchers.IO).launch {
                groupViewModel.updateGroup(group = newGroup,
                    convertFunction = { convertToJpeg(context = context, uri = uri) },
                    onComplete = {
//                        Toast.makeText(context, "Group updated", Toast.LENGTH_SHORT).show()
                    })
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun EditGroupScreen(group: State<Group?>, loadState: State<LoadState>, modifier: Modifier = Modifier,
                    updateGroup: (Group, Uri?, Context) -> Unit) {
    val TAG = "EditGroupScreen"
    val context = LocalContext.current
    var groupName by remember { mutableStateOf(group.value?.name ?: "") }
    var groupDescription by remember { mutableStateOf(group.value?.description ?: "") }
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    var newUri by remember { mutableStateOf<Uri?>(null) }
    var deleteFlag by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            newUri = uri
            deleteFlag = false
        }
    }
    val appContext = LocalContext.current

    if (loadState.value == LoadState.Loading) {
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
    }

    if (group.value != null) {

        Scaffold { padding ->
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row {
                    RoundImage(
                        model = if (deleteFlag) null else if (newUri == null) group.value!!.groupPhotoUrl else newUri,
                        modifier = Modifier
                            .size(125.dp)
                            .clickable {
                                dropDownMenuExpanded = true
                            },
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
                                deleteFlag = true
                            }
                        )
                    }
                }

                Spacer(Modifier.height(15.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = groupName,
                    onValueChange = {
                        if (it.length <= maxCharsForUsername) {
                            groupName = it
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default
                        .copy(capitalization = KeyboardCapitalization.Sentences)
                )

                Spacer(Modifier.height(25.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = groupDescription,
                    onValueChange = {
                        if ((it.length <= maxCharsForDescription && it.count { ch -> ch == '\n' } <= maxLinesForDescription)) {
                            groupDescription = it
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default
                        .copy(capitalization = KeyboardCapitalization.Sentences)
                )

                Spacer(Modifier.height(15.dp))

                Button(
                    onClick = {
                        Log.d(TAG, "Save changes clicked")
                        if (newUri != null || deleteFlag) {
                            Log.d(TAG, "EditGroupScreen: Coil cleared")
                            appContext.imageLoader.diskCache?.clear()
                            appContext.imageLoader.memoryCache?.clear()
                        }
                        if (((groupName == group.value!!.name && groupDescription == group.value!!.description) || (groupName.isEmpty() || groupDescription.isEmpty())) && (newUri == null && !deleteFlag)) {
                            Toast.makeText(context, "No Changes made", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val newGroup = group.value!!.copy(name = groupName, description = groupDescription)
                        updateGroup(newGroup, if (deleteFlag) null else newUri, context)
                        Log.d(TAG, "Save changes onClick ended")
                    }
                ) {
                    Text("Save changes")
                }


            }
        }
    }
}

@PreviewLightDark
@Composable
fun EditGroupScreenPreview(modifier: Modifier = Modifier) {
    val groupState = remember { mutableStateOf<Group?>(
        Group(
            name = "Group",
            description = "Description"
        )
    ) }
    val loadState = remember { mutableStateOf(LoadState.Idle) }
    ChatterBoxTheme {
        EditGroupScreen(
            group = groupState,
            loadState = loadState,
            updateGroup = {group, uri, context ->},
        )
    }
}