package com.example.chatterbox.core.common

import android.text.format.DateUtils
import com.google.firebase.auth.FirebaseAuth


object FirestoreCollections {
    const val USERS = "users"
    const val USERCHATS = "userchats"
    const val CHATS = "chats"
}

fun getRelativeTime(millis: Long): CharSequence {
    return DateUtils.getRelativeTimeSpanString(
        millis,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    )
}
