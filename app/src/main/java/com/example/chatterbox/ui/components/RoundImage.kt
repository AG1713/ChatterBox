package com.example.chatterbox.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun RoundImage(image: Painter, showDot: Boolean, modifier: Modifier = Modifier) {

    Box {

        Image(
            modifier = modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = CircleShape
                )
                .padding(1.dp)
                .clip(CircleShape),
            painter = image,
            contentDescription = "Profile Picture"
        )

        if (showDot) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .offset(-10.dp, -10.dp)
                    .background(Color(0xFF4CAF50), CircleShape)
                    .border(2.dp, Color.White, CircleShape) // outline for separation
            )
        }
    }
}