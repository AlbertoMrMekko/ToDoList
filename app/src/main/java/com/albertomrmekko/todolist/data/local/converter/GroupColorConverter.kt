package com.albertomrmekko.todolist.data.local.converter

import androidx.room.TypeConverter
import com.albertomrmekko.todolist.domain.model.GroupColor

class GroupColorConverter {
    @TypeConverter
    fun fromGroupColor(color: GroupColor): String = color.name

    @TypeConverter
    fun toGroupColor(value: String): GroupColor = GroupColor.valueOf(value)
}