package com.albertomrmekko.todolist.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateConverter {
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(date: String?): LocalDateTime? {
        return date?.let(LocalDateTime::parse)
    }
}