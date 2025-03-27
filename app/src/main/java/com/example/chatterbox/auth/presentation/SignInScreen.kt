package com.example.chatterbox.auth.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.ChatterBoxTheme

const val TAG: String = "SignInScreen"

@Composable
fun SignInScreen(modifier: Modifier = Modifier) {

    var emailState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Welcome to ChatterBox!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.fillMaxHeight(0.1f))

        Text(
            text = "Sign in using Email to continue",
            fontSize = 24.sp,

        )
        Spacer(Modifier.height(50.dp))
        TextField(
            value = emailState,
            onValueChange = { it:String ->
                emailState = it
            },
            label = { Text("Enter your Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(15.dp))
        TextField(
            value = passwordState,
            onValueChange = { it:String ->
                passwordState = it
            },
            label = { Text("Enter your password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(25.dp))
        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = {
                Log.d(TAG, "SignInScreen: Button Clicked")
            }
        ) {
            Text(text = "Sign in")
        }


    }


}


@PreviewLightDark
@Composable
fun SignInScreenPreview(modifier: Modifier = Modifier) {
    ChatterBoxTheme {

        SignInScreen()
    }
}