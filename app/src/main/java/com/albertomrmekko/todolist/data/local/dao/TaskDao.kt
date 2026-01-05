package com.albertomrmekko.todolist.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.albertomrmekko.todolist.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE groupId = :groupId ORDER BY id ASC")
    fun getTasksByGroupId(groupId: Long): Flow<List<TaskEntity>>

    @Insert
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}