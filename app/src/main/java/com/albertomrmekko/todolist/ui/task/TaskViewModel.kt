package com.albertomrmekko.todolist.ui.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertomrmekko.todolist.data.local.entity.TaskEntity
import com.albertomrmekko.todolist.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TaskRepository
) : ViewModel() {
    private val groupId: Long = checkNotNull(savedStateHandle["groupId"])

    val tasks: StateFlow<List<TaskEntity>> = repository.getTasksByGroupId(groupId)
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5_000),
                emptyList()
                )

    fun addTask(message: String) {
        viewModelScope.launch {
            repository.addTask(groupId, message)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}