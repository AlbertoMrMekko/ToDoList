package com.albertomrmekko.todolist.ui.group

import androidx.compose.ui.graphics.Color
import com.albertomrmekko.todolist.domain.model.GroupColor

fun GroupColor.toColor(): Color =
    when (this) {
        GroupColor.YELLOW -> Color.Companion.Yellow
        GroupColor.RED -> Color.Companion.Red
        GroupColor.BLUE -> Color.Companion.Cyan
        GroupColor.GREEN -> Color.Companion.Green
        GroupColor.PURPLE -> Color(0xFF9C27B0)
        GroupColor.ORANGE -> Color(0xFFFF9800)
        GroupColor.BROWN -> Color(0xFF795548)
        GroupColor.PINK -> Color(0xFFE91E63)
    }