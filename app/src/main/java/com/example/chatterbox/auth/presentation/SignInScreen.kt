package com.example.chatterbox.auth.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chatterbox.ChatPagerScreenObject
import com.example.chatterbox.CreateUserScreenObject
import com.example.chatterbox.R
import com.example.chatterbox.SignInScreenObject
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val TAG: String = "SignInScreen"

@Composable
fun SignInRoot(authViewModel: AuthViewModel, navController: NavController, modifier: Modifier = Modifier) {

    SignInScreen(authViewModel = authViewModel, navController = navController)
}

@Composable
fun SignInScreen(authViewModel: AuthViewModel, navController: NavController?, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)

    authViewModel.signOut() // Because we cannot navigate first or sign out first from the
    // the main screen, since, if we navigate first, rest sign out code won't run, and
    // if we log out first, app will crash with null pointer exception since current user id is null

    // The below is to ask for account always
    googleSignInClient.revokeAccess().addOnCompleteListener {
        Log.d("AuthViewModel", "User signed out and account selection reset")
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)

                // Retrieving id
                val idToken = account?.idToken
                if (idToken != null){
                    authViewModel.viewModelScope.launch {
                        authViewModel.signInWithGoogle(idToken){ user ->
                            Log.d(TAG, "SignInScreen: $user")
                            if (user == null) {
                                navController?.navigate(CreateUserScreenObject) {
                                    popUpTo<SignInScreenObject> { inclusive = true }
                                }
                            }
                            else {
                                navController?.navigate(ChatPagerScreenObject) {
                                    popUpTo<SignInScreenObject> { inclusive = true }
                                }
                            }
                        }

                    }
                }

            }
            catch (e: ApiException){
                Log.d(TAG, "ApiException: ${e.message}")
            }

        }

    }

    val authState by authViewModel.authState.collectAsState()

    ChatterBoxTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Let's get up signed up!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.fillMaxHeight(0.05f))
            Text(
                text = "Sign In using Google to continue",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(25.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = Color(0xFF3367D6),
                    contentColor = Color(0xFFFFFFFF),
                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer
                ),
                onClick = { signInWithGoogle(context, launcher) }
            ) {
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth(),
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "Google logo",
                        modifier = Modifier.fillMaxHeight(), // Fills the container
                        tint = Color.Unspecified // Use original colors
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Sign in with Google",
                    )
                }
            }
        }

        if (authState == AuthState.Loading) {
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


    }

}

fun signInWithGoogle(
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)
}

@PreviewLightDark
@Composable
fun SignInScreenPreview(modifier: Modifier = Modifier) {
    val authViewModel = koinViewModel<AuthViewModel>()
    ChatterBoxTheme {
        SignInScreen(authViewModel = authViewModel, navController = null)
    }
}