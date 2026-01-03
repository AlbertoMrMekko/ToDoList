package com.albertomrmekko.todolist.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.data.local.entity.TaskEntity

data class GroupWithTasks (
    @Embedded val group: GroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val tasks: List<TaskEntity>
)