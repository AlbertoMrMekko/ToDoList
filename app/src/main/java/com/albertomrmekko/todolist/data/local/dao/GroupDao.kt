package com.albertomrmekko.todolist.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM `groups`")
    fun getGroups(): Flow<List<GroupEntity>>

    @Insert
    suspend fun insert(group: GroupEntity)
}