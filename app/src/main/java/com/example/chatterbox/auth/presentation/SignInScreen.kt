package com.example.chatterbox.auth.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatterbox.R
import com.example.compose.ChatterBoxTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

const val TAG: String = "SignInScreen"

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)

//    authViewModel.signOut() // Already there in MainActivity
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
                    authViewModel.signInWithGoogle(idToken)
                }

            }
            catch (e: ApiException){
                Log.d(TAG, "ApiException: ${e.message}")
            }

        }

    }

    ChatterBoxTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Let's get up signed up!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.fillMaxHeight(0.05f))
            Text(
                text = "Sign In using Google to continue",
                fontSize = 16.sp,
            )
            Spacer(Modifier.height(25.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
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
                        text = "Sign in with Google"
                    )
                }
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
    ChatterBoxTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Let's get up signed up!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.fillMaxHeight(0.05f))
            Text(
                text = "Sign In using Google to continue",
                fontSize = 24.sp,
            )
            Spacer(Modifier.height(25.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {  }
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
                        text = "Sign in with Google"
                    )
                }
            }
        }
    }
}