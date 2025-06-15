package com.example.chatterbox.chat.groups.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatterbox.chat.groups.domain.Group
import com.example.chatterbox.chat.groups.domain.GroupRepository
import com.example.chatterbox.chat.shared.domain.Member
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupViewModel(
    private val groupRepository: GroupRepository
): ViewModel() {
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

    fun createGroup(currentUsername: String, name: String, photoUrl: String, description: String, onComplete: (String, String) -> Unit) {
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            val groupId = groupRepository.createGroup(
                currentUsername = currentUsername,
                name = name,
                photoUrl = photoUrl,
                description = description
            )
            _loadState.value = LoadState.Idle

            onComplete(groupId, name)
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