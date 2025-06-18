package com.example.chatterbox.chat.groups.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.BuildConfig
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.groups.domain.GroupRepository
import com.example.chatterbox.chat.shared.domain.Member
import com.example.chatterbox.chat.shared.domain.StorageRepository
import com.example.chatterbox.core.common.SupabaseBuckets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupViewModel(
    private val groupRepository: GroupRepository,
    private val storageRepository: StorageRepository
): ViewModel() {
    val TAG = "GroupViewModel"
    val groups = groupRepository.groups
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Idle)
    val loadState = _loadState.asStateFlow()
    private val _currentGroup = MutableStateFlow<Group?>(null)
    val currentGroup = _currentGroup.asStateFlow()

    fun getAllGroups(){
        viewModelScope.launch {
            groupRepository.getAllGroups()
        }
    }

    fun getGroup(groupId: String){
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            _currentGroup.value = groupRepository.getGroup(groupId)
            _loadState.value = LoadState.Idle
        }
    }

    fun clearCurrentGroupStateFlow(){
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            _currentGroup.value = null
            _loadState.value = LoadState.Idle
        }
    }

    fun clearListeners(){
        viewModelScope.launch {
            groupRepository.clearListeners()
        }
    }

    fun createGroup(currentUsername: String, name: String, description: String, convertFunction: () -> ByteArray?, onComplete: (String, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadState.value = LoadState.Loading
            var byteArray: ByteArray? = null
            byteArray = convertFunction()

            val groupId = groupRepository.createGroup(
                currentUsername = currentUsername,
                name = name,
                description = description
            )

            if (byteArray != null) {
                storageRepository.uploadImage(bucket = SupabaseBuckets.GROUP_PHOTOS, bytes = byteArray, fileName = groupId)
            }
            _loadState.value = LoadState.Idle

            onComplete(groupId, name)
        }
    }

    fun updateGroup(group: Group, convertFunction: () -> ByteArray?, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadState.value = LoadState.Loading
            var byteArray: ByteArray? = null

            byteArray = convertFunction()
            // Delegating a function to perform 'convertToJpeg' since,
            // 1. Loading remains consistent
            // 2. Context should not be passed to viewmodel

            try {

                if (byteArray == null) {
                    storageRepository.deleteImage(bucket = SupabaseBuckets.GROUP_PHOTOS, fileName = group.id)
                    groupRepository.updateGroup(group.copy( groupPhotoUrl = "" ))
                }
                else {
                    storageRepository.uploadImage(bucket = SupabaseBuckets.GROUP_PHOTOS, bytes = byteArray, fileName = group.id)
                    groupRepository.updateGroup(group.copy( groupPhotoUrl = "${BuildConfig.SUPABASE_URL}storage/v1/object/public/${SupabaseBuckets.GROUP_PHOTOS}/${group.id}.jpg" ))
                }

                getGroup(groupId = group.id)
                _loadState.value = LoadState.Idle
                onComplete()
            } catch (e: Exception) {
                Log.e(TAG, "Error in updateCurrentUser", e)
            }

        }
    }

    fun addMembers(groupId: String, memberIds: List<String>, members: List<Member>) {
        _loadState.value = LoadState.Loading
        viewModelScope.launch {
            groupRepository.addMembers(
                groupId = groupId,
                memberIds = memberIds,
                members = members
            )
        }
        _loadState.value = LoadState.Idle
    }

    fun leaveGroup(groupId: String, userId: String, username: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            groupRepository.leaveGroup(
                groupId = groupId,
                userId = userId,
                username = username
            )
            onComplete()
            _loadState.value = LoadState.Idle
        }
    }

}

sealed class LoadState() {
    data object Idle: LoadState()
    data object Loading: LoadState()
    data class Error(val exception: Throwable?): LoadState()
}