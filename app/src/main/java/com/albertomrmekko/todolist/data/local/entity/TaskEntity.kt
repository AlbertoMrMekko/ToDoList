package com.albertomrmekko.todolist.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId")]
)
data class TaskEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val groupId: Long,
    val message: String,
    var completed: Boolean = false
)