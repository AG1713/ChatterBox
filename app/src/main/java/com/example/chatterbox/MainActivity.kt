package com.example.chatterbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatterbox.auth.presentation.AuthViewModel
import com.example.chatterbox.chat.userChats.domain.Member
import com.example.chatterbox.chat.userChats.domain.UserChat
import com.example.chatterbox.chat.userChats.presentation.UserChatItem
import com.example.chatterbox.core.common.FirestoreCollections
import com.example.chatterbox.ui.theme.ChatterBoxTheme
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity" // TODO: Change this name later

    private val authViewModel: AuthViewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        authViewModel.signOut()

//        FirebaseFirestore.getInstance().collection(FirestoreCollections.USERCHATS)
//            .document("1")
//            .set(
//                    UserChat(
//                        id = "1",
//                        members = mutableListOf(Member("1", "User1"), Member("2","User1234")),
//                        memberIds = mutableListOf("1", "NhZ187ifEKQSkk3Fkq5navKMBqG2"),
//                        lastMessageTime = System.currentTimeMillis(),
//                        lastMessageUserId = "1",
//                        lastMessageUsername = "User1",
//                        lastMessage = "Hello"
//                    )
//                )

        setContent {
            ChatterBoxTheme {
                AuthNavigator()
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatterBoxTheme {
        Greeting("Android")
    }
}