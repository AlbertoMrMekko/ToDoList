package com.albertomrmekko.todolist.data.repository

import com.albertomrmekko.todolist.data.local.dao.GroupDao
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupDao: GroupDao
) {
    fun getGroups(): Flow<List<GroupEntity>> = groupDao.getGroups()

    suspend fun addGroup(name: String) {
        groupDao.insert(GroupEntity(name = name))
    }
}