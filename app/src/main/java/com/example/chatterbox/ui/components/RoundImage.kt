package com.example.chatterbox.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatterbox.R
import com.example.chatterbox.ui.theme.ChatterBoxTheme

@Composable
fun RoundImage(model: Any?, modifier: Modifier = Modifier, showDot: Boolean = false, editable: Boolean = false) {

    // Type validation of model is handled by AsyncImage appropriately
    Box {
        AsyncImage(
            modifier = modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = CircleShape
                )
                .padding(1.dp)
                .clip(CircleShape),
            model = model,
            placeholder = painterResource(R.drawable.default_user_photo),
            error = painterResource(R.drawable.default_user_photo),
            contentDescription = "Image",
            contentScale = ContentScale.Crop
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

        if (editable) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit icon",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd)
                    .offset(-10.dp, -10.dp)
                    .background(Color(0xFF4CAF50), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )// outline for separation
        }

    }
}

@PreviewLightDark
@Composable
fun RoundImagePreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {
        RoundImage(
            model = null,
            showDot = false,
            editable = true
        )
    }
}