package com.albertomrmekko.todolist.ui.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.data.repository.GroupRepository
import com.albertomrmekko.todolist.domain.model.GroupColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : ViewModel() {
    val groups: StateFlow<List<GroupEntity>> = repository.getGroups().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addGroup(name: String, color: GroupColor) {
        viewModelScope.launch {
            repository.addGroup(name, color)
        }
    }

    fun updateGroup(group: GroupEntity) {
        viewModelScope.launch {
            repository.updateGroup(group)
        }
    }

    fun deleteGroup(group: GroupEntity) {
        viewModelScope.launch {
            repository.deleteGroup(group)
        }
    }
}