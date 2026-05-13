package com.albertomrmekko.todolist.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HourMinutePicker(
    selectedHour: Int,
    selectedMinute: Int,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalNumberPicker(
            valueRange = 0..23,
            selectedValue = selectedHour,
            onValueSelected = onHourSelected,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = ":",
            fontSize = 28.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        VerticalNumberPicker(
            valueRange = 0..59,
            selectedValue = selectedMinute,
            onValueSelected = onMinuteSelected,
            modifier = Modifier.weight(1f)
        )
    }
}
