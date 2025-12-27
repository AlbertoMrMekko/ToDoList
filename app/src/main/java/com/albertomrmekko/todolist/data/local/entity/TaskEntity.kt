package com.albertomrmekko.todolist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    var completed: Boolean = false
)