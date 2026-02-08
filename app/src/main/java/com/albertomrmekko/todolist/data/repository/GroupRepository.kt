package com.albertomrmekko.todolist.data.repository

import com.albertomrmekko.todolist.data.local.dao.GroupDao
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.data.local.relation.GroupWithActiveTaskCount
import com.albertomrmekko.todolist.domain.model.GroupColor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupDao: GroupDao
) {
    fun getGroupsWithActiveTaskCount(): Flow<List<GroupWithActiveTaskCount>> =
        groupDao.getGroupsWithActiveTaskCount()

    fun getById(id: Long): Flow<GroupEntity?> = groupDao.getById(id)

    suspend fun addGroup(name: String, color: GroupColor) =
        groupDao.insert(GroupEntity(name = name, color = color))

    suspend fun updateGroup(group: GroupEntity) = groupDao.update(group)

    suspend fun deleteGroup(group: GroupEntity) = groupDao.delete(group)
}