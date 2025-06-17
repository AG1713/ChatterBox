package com.example.chatterbox

import android.util.Log
import com.example.chatterbox.core.common.FirestoreCollections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object HeartbeatManager {

    private var heartbeatJob: Job? = null

    fun startHeartbeat(userId: String) {
        heartbeatJob?.cancel()

        heartbeatJob = CoroutineScope(Dispatchers.IO).launch {
            FirebaseFirestore.getInstance().collection(FirestoreCollections.USERS)
                .document(userId).update(
                    "lastActive", System.currentTimeMillis()
                )
            Log.d("HeartbeatManager", "lastActive time updated")
            delay(3_00_000)
        }

    }

    fun stopHeartbeat(){
        heartbeatJob?.cancel()
    }

}