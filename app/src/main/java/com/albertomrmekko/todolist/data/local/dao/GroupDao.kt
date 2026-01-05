package com.albertomrmekko.todolist.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM `groups` ORDER BY id ASC")
    fun getGroups(): Flow<List<GroupEntity>>

    @Insert
    suspend fun insert(group: GroupEntity)

    @Update
    suspend fun update(task: GroupEntity)

    @Delete
    suspend fun delete(task: GroupEntity)
}