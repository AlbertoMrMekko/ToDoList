package com.albertomrmekko.todolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.albertomrmekko.todolist.data.local.converter.GroupColorConverter
import com.albertomrmekko.todolist.data.local.dao.GroupDao
import com.albertomrmekko.todolist.data.local.dao.TaskDao
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, GroupEntity::class],
    version = 1
)
@TypeConverters(GroupColorConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun groupDao(): GroupDao
}