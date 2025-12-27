package com.albertomrmekko.todolist.data.repository

import com.albertomrmekko.todolist.data.local.dao.TaskDao
import com.albertomrmekko.todolist.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getTasks(): Flow<List<TaskEntity>> = taskDao.getTasks()

    suspend fun addTask(message: String) {
        taskDao.insert(TaskEntity(message = message))
    }
}