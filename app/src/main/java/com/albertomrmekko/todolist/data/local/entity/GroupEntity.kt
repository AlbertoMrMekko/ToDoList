package com.albertomrmekko.todolist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.albertomrmekko.todolist.domain.model.GroupColor

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: GroupColor
)