package com.example.chatterbox.auth.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatterbox.ChatPagerScreenObject
import com.example.chatterbox.CreateUserScreenObject
import com.example.chatterbox.SignInScreenObject
import com.example.chatterbox.core.common.maxCharsForDescription
import com.example.chatterbox.core.common.maxCharsForUsername
import com.example.chatterbox.core.common.maxLinesForDescription
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CreateUserRoot(authViewModel: AuthViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val authState = authViewModel.authState.collectAsStateWithLifecycle()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    val email = currentUser?.email
    authViewModel.signOut()

    CreateUserScreen(
        userId = userId,
        email = email,
        authState = authState,
        createUser = { username, description ->
            authViewModel.createUser(userId, email, username, description) {
                navController.navigate(SignInScreenObject) {
                    popUpTo<CreateUserScreenObject>{ inclusive=true }
                }
            }
        })
}

@Composable
fun CreateUserScreen(userId: String?, email: String?, authState: State<AuthState>, createUser: (String, String)-> Unit,  modifier: Modifier = Modifier) {
    var usernameState by remember {
        mutableStateOf("")
    }
    var descriptionState by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    if (authState.value == AuthState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // faded background
                .clickable(enabled = false) {} // disables clicks
                .zIndex(1f), // ensures it's on top
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Welcome to ChatterBox!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.fillMaxHeight(0.1f))

        Text(
            text = "Let's create you a profile",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(50.dp))
        TextField(
            value = usernameState,
            onValueChange = { it: String ->
                if (it.length <= maxCharsForUsername) {
                    usernameState = it
                }
            },
            label = { Text("Enter your username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default
                .copy(capitalization = KeyboardCapitalization.Sentences),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Note: username cannot be changed later",
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(15.dp))
        TextField(
            value = descriptionState,
            onValueChange = { it: String ->
                if (it.length <= maxCharsForDescription && it.count { ch -> ch == '\n' } <= maxLinesForDescription) {
                    descriptionState = it
                }
            },
            label = { Text("Describe yourself") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default
                .copy(capitalization = KeyboardCapitalization.Sentences),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(25.dp))
        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = {
                if (usernameState.trim().isBlank() || descriptionState.trim().isBlank()) {
                    Toast.makeText(context, "Please all field", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context, "Sign in again to continue", Toast.LENGTH_SHORT).show()
                    createUser(usernameState, descriptionState)
                }
            }
        ) {
            Text(text = "Sign in")
        }
    }
}

@PreviewLightDark
@Composable
fun CreateUserScreenPreview(modifier: Modifier = Modifier) {
    val authState = remember { mutableStateOf(AuthState.Idle) }
    ChatterBoxTheme {
        CreateUserScreen(
            authState = authState,
            createUser = { username, description -> },
            userId = "1",
            email = "sample@gmail.com"
        )
    }
}