package com.albertomrmekko.todolist.data.repository

import com.albertomrmekko.todolist.data.local.dao.TaskDao
import com.albertomrmekko.todolist.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getTasksByGroupId(groupId: Long): Flow<List<TaskEntity>> =
        taskDao.getTasksByGroupId(groupId)

    suspend fun addTask(
        groupId: Long,
        message: String,
        reminderDate: String?,
        reminderTime: String?
    ) =
        taskDao.insert(
            TaskEntity(
                groupId = groupId,
                message = message,
                reminderDate = reminderDate,
                reminderTime = reminderTime
            )
        )

    suspend fun updateTask(task: TaskEntity) = taskDao.update(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.delete(task)

    suspend fun setCompleted(task: TaskEntity, completed: Boolean) {
        taskDao.updateCompleted(task.id, completed)
    }
}