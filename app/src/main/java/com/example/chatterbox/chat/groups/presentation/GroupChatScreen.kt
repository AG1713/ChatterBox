package com.example.chatterbox.chat.groups.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.GroupInfoRootObject
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.shared.domain.Message
import com.example.chatterbox.chat.userChats.presentation.MessageItem
import com.example.chatterbox.ui.theme.ChatterBoxTheme

@Composable
fun GroupChatRoot(groupChatViewModel: GroupChatViewModel, groupId: String, groupName: String, currentUserId: String,
                  currentUsername: String, navController: NavController, modifier: Modifier = Modifier) {
    val messages by groupChatViewModel.messages.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        groupChatViewModel.getAllMessages(groupId)
        onDispose {
            groupChatViewModel.exitChat()
        }
    }

    Log.d("Debug", "GroupChatRoot: $messages")
    GroupChatScreen(
        messages = messages,
        groupId = groupId,
        groupName = groupName,
        currentUserId = currentUserId,
        navController = navController
    ) { text ->
        groupChatViewModel.sendMessage(groupId = groupId, text = text, senderUsername = currentUsername)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen (
    messages: List<Message>,
    groupId: String,
    groupName: String,
    currentUserId: String,
    navController: NavController?,
    modifier: Modifier = Modifier,
    sendMessage: (String) -> Unit
){
    var messageText by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.clickable {
                    navController?.navigate(GroupInfoRootObject(groupId = groupId, groupName = groupName))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text(
                        text = groupName,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },

        bottomBar = {
            val maxChars = 256
            val maxLines = 32
            val minTextFieldHeight = 56.dp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { newValue ->
                        val text = newValue.text
                        val lineCount = text.count { it == '\n' } + 1

                        if (text.length <= maxChars && lineCount <= maxLines) {
                            messageText = newValue
                        }
                    },
                    placeholder = { Text("Type a message") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    maxLines = 5,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = minTextFieldHeight),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = {
                        if (messageText.text.isNotEmpty()) sendMessage(messageText.text.trim())
                        messageText = TextFieldValue("")
                    },
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send button",
                        tint = Color.White,
                        modifier = Modifier
                            .background(Color(0xFF388E3C), shape = RoundedCornerShape(50))
                            .padding(10.dp)
                    )
                }
            }
        }
    ) { padding ->

        if (messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No chats found",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
        else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(5.dp),
                reverseLayout = true
            ) {

                items(messages) {
                    MessageItem(
                        currentUserId = currentUserId,
                        message = it
                    )
                    Spacer(Modifier.height(5.dp))
                }


            }
        }

    }

}

@PreviewLightDark
@Composable
fun GroupChatScreenPreview(modifier: Modifier = Modifier) {
    val sampleMessages = listOf(
        Message(text = "Yes, let's meet at 6 PM.", senderId = "u1", senderUsername = "Alice"),
        Message(text = "Are we still meeting today?", senderId = "u2", senderUsername = "Bob"),
        Message(text = "Doing great, thanks for asking.", senderId = "u1", senderUsername = "Alice"),
        Message(text = "I'm good! What about you?", senderId = "u2", senderUsername = "Bob"),
        Message(text = "Hey, how are you?", senderId = "u1", senderUsername = "Alice"),
    )

    ChatterBoxTheme {
        GroupChatScreen(
            messages = sampleMessages,
            groupId = "g1",
            groupName = "Group",
            currentUserId = "u1",
            navController = null,
            sendMessage = {}
        )
    }
}