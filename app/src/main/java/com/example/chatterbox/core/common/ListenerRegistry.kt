package com.example.chatterbox.core.common

import android.util.Log
import com.google.firebase.firestore.ListenerRegistration

object ListenerRegistry {
    private val listeners = mutableMapOf<String, ListenerRegistration>()

    object ListenerKeys {
        const val USER_CHATS_LISTENER = "USER_CHATS_LISTENER"
        const val CHATS_LISTENER = "CHATS_LISTENER"
        const val GROUPS_LISTENER = "GROUPS_LISTENER"
        const val GROUP_CHATS_LISTENER = "GROUP_CHATS_LISTENER"
    }

    fun register(key: String, registration: ListenerRegistration){
        listeners[key]?.remove()
        listeners[key] = registration
    }

    fun remove(key: String){
        listeners[key]?.remove()
        listeners.remove(key)
    }

    fun clearAll(){
        listeners.values.forEach{
            it.remove()
        }
        listeners.clear()
        Log.d("ListenerRegistry", "Cleared all listeners")
    }

}