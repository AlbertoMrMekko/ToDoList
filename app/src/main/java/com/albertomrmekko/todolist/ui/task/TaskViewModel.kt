package com.albertomrmekko.todolist.ui.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.data.local.entity.TaskEntity
import com.albertomrmekko.todolist.data.repository.GroupRepository
import com.albertomrmekko.todolist.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val groupRepository: GroupRepository
) : ViewModel() {
    private val groupId: Long = checkNotNull(savedStateHandle["groupId"])

    private val group: StateFlow<GroupEntity?> = groupRepository.getById(groupId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )
    private val tasks: StateFlow<List<TaskEntity>> = taskRepository.getTasksByGroupId(groupId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    val uiState: StateFlow<TaskUiState> = combine(group, tasks) { group, tasks ->
        TaskUiState(
            group = group,
            activeTasks = tasks.filter { !it.completed },
            completedTasks = tasks.filter { it.completed }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TaskUiState()
    )

    fun addTask(message: String, reminderDate: LocalDate?, reminderTime: LocalTime?) {
        viewModelScope.launch {
            taskRepository.addTask(
                groupId,
                message,
                reminderDate?.toString(),
                reminderTime?.toString()
            )
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun toggleCompleted(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.setCompleted(task, !task.completed)
        }
    }
}