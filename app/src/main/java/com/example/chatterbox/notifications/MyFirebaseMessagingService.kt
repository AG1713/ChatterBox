package com.example.chatterbox.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.chatterbox.core.common.DEFAULT_CHANNEL
import com.example.chatterbox.core.common.FirestoreCollections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title
        val body = message.notification?.body

        if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
            showNotification(title, body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null) {
            FirebaseFirestore.getInstance()
                .collection(FirestoreCollections.USERS)
                .document(currentUserId)
                .update("messageToken", token)
        }

    }

    private fun showNotification(title: String?, message: String?) {
        val builder = NotificationCompat.Builder(this, DEFAULT_CHANNEL)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }

}