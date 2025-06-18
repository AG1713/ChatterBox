package com.example.chatterbox.core.common

import android.text.format.DateUtils
import com.google.firebase.auth.FirebaseAuth


object FirestoreCollections {
    const val USERS = "users"
    const val USERCHATS = "userchats"
    const val CHATS = "chats"
    const val GROUPS = "groups"
}

fun getRelativeTime(millis: Long): CharSequence {
    return DateUtils.getRelativeTimeSpanString(
        millis,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    )
}

public val DEFAULT_CHANNEL = "default_channel"
val maxCharsForUsername = 15
val maxCharsForDescription = 50
val maxLinesForDescription = 5

object SupabaseBuckets {
    const val USER_PHOTOS = "user-photos"
    const val GROUP_PHOTOS = "group-photos"
}
