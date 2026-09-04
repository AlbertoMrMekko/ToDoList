package com.albertomrmekko.todolist.ui.group

import androidx.compose.ui.graphics.Color
import com.albertomrmekko.todolist.domain.model.GroupColor

fun GroupColor.toColor(): Color =
    when (this) {
        GroupColor.YELLOW -> Color(0xFFFFC107)  // Amber
        GroupColor.RED -> Color(0xFFF44336)     // Red
        GroupColor.BLUE -> Color(0xFF2196F3)    // Blue
        GroupColor.GREEN -> Color(0xFF4CAF50)   // Green
        GroupColor.PURPLE -> Color(0xFF9C27B0)  // Purple
        GroupColor.ORANGE -> Color(0xFFFF9800)  // Orange
        GroupColor.BROWN -> Color(0xFF795548)   // Brown
        GroupColor.PINK -> Color(0xFFE91E63)    // Pink
    }