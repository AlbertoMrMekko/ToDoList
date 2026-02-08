package com.albertomrmekko.todolist.data.local.relation

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.albertomrmekko.todolist.data.local.entity.GroupEntity

data class GroupWithActiveTaskCount(
    @Embedded val group: GroupEntity,
    @ColumnInfo(name = "activeTaskCount") val activeTaskCount: Int
)