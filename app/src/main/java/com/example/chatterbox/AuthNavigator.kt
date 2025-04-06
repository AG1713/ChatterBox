package com.example.chatterbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.chatterbox.auth.presentation.AuthViewModel
import com.example.chatterbox.auth.presentation.SignInScreen
import com.example.chatterbox.chat.presentation.ChatPagerScreen

@Composable
fun AuthNavigator(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    val user by authViewModel.user.collectAsState()

    if (user != null) {
        ChatPagerScreen()
    }
    else {
        SignInScreen(authViewModel)
    }
}