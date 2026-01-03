package com.albertomrmekko.todolist.data.repository

import com.albertomrmekko.todolist.data.local.dao.TaskDao
import com.albertomrmekko.todolist.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getTasks(): Flow<List<TaskEntity>> = taskDao.getTasks()

    fun getTasksByGroupId(groupId: Long): Flow<List<TaskEntity>> = taskDao.getTasksByGroupId(groupId)

    suspend fun addTask(groupId: Long, message: String) =
        taskDao.insert(TaskEntity(groupId = groupId, message = message))

    suspend fun updateTask(task: TaskEntity) = taskDao.update(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.delete(task)
}