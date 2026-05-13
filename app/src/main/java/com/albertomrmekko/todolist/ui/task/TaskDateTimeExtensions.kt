package com.albertomrmekko.todolist.ui.task

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun TaskDateTimeUiState.toLocalDateTime(): LocalDateTime? {
    if (!dateEnabled && !timeEnabled) {
        return null
    }

    val finalDate = if (dateEnabled) {
        selectedDate ?: LocalDate.now()
    } else {
        LocalDate.now()
    }

    val finalTime = if (timeEnabled) {
        LocalTime.of(selectedHour, selectedMinute)
    } else {
        LocalTime.MIDNIGHT
    }

    return LocalDateTime.of(finalDate, finalTime)
}