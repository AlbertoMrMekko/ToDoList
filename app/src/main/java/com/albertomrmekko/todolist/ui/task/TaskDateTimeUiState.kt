package com.albertomrmekko.todolist.ui.task

import java.time.LocalDate

data class TaskDateTimeUiState(
    val dateEnabled: Boolean = false,
    val timeEnabled: Boolean = false,

    val selectedDate: LocalDate? = null,
    val selectedHour: Int = 0,
    val selectedMinute: Int = 0
)