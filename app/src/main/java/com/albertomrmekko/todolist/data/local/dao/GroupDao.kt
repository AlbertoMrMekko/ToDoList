package com.albertomrmekko.todolist.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.data.local.relation.GroupWithActiveTaskCount
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT g.*, COUNT(t.id) AS activeTaskCount FROM `groups` g LEFT JOIN tasks t ON t.groupId = g.id AND t.completed = 0 GROUP BY g.id")
    fun getGroupsWithActiveTaskCount(): Flow<List<GroupWithActiveTaskCount>>

    @Query("SELECT * FROM `groups` WHERE id = :id")
    fun getById(id: Long): Flow<GroupEntity?>

    @Insert
    suspend fun insert(group: GroupEntity)

    @Update
    suspend fun update(task: GroupEntity)

    @Delete
    suspend fun delete(task: GroupEntity)
}