package com.albertomrmekko.todolist.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.albertomrmekko.todolist.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun insert(task: TaskEntity)
}