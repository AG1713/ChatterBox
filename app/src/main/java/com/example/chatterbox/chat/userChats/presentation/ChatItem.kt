package com.example.chatterbox.chat.userChats.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.chatterbox.chat.shared.domain.Message
import com.example.chatterbox.core.common.getRelativeTime
import com.example.chatterbox.ui.theme.ChatterBoxTheme

@Composable
fun MessageItem(modifier: Modifier = Modifier, currentUserId: String, message: Message) {

    val yourMessage = (message.senderId == currentUserId)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (yourMessage) 15.dp else 0.dp,
                end = if (yourMessage) 0.dp else 15.dp
            )
    ) {

        ChatBubble(
            modifier = modifier
                .background(
                    color = if (yourMessage) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp)
                )
                .align(
                    if (yourMessage) Alignment.CenterEnd else Alignment.CenterStart
                ),
            text = message.text,
            textColor = if (yourMessage) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface,
            sender = if (yourMessage) "You" else message.senderUsername,
            time = message.time
        )

    }

}

@Composable
fun ChatBubble(modifier: Modifier = Modifier, text: String, textColor: Color, sender: String, time: Long) {
    Column(
        modifier = modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
    ){
        Text(
            text = sender,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
        Text(
            text = getRelativeTime(time).toString(),
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}

@PreviewLightDark
@Composable
fun MessageItemPreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {
        MessageItem(
            currentUserId = "u1",
            message = Message(text = "Hey, how are you?", senderId = "u1", senderUsername = "Alice")
        )
    }
}