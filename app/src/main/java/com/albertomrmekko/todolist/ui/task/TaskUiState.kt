package com.albertomrmekko.todolist.ui.task

import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.data.local.entity.TaskEntity

data class TaskUiState(
    val group: GroupEntity? = null,
    val activeTasks: List<TaskEntity> = emptyList(),
    val completedTasks: List<TaskEntity> = emptyList()
)