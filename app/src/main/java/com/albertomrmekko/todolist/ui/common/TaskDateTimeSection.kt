package com.albertomrmekko.todolist.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.albertomrmekko.todolist.ui.task.TaskDateTimeUiState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TaskDateTimeSection(
    state: TaskDateTimeUiState,
    onStateChange: (TaskDateTimeUiState) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Día",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = state.dateEnabled,
                onCheckedChange = { enabled ->

                    onStateChange(
                        state.copy(
                            dateEnabled = enabled,
                            selectedDate = if (enabled) {
                                state.selectedDate ?: LocalDate.now()
                            } else {
                                null
                            }
                        )
                    )
                }
            )
        }

        if (state.dateEnabled) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = { showDatePicker = true }
            ) {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                Text(
                    text = state.selectedDate?.format(formatter) ?: "Seleccionar fecha"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hora",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = state.timeEnabled,
                onCheckedChange = { enabled ->
                    onStateChange(state.copy(timeEnabled = enabled))
                }
            )
        }

        if (state.timeEnabled) {
            Spacer(modifier = Modifier.height(16.dp))

            HourMinutePicker(
                selectedHour = state.selectedHour,
                selectedMinute = state.selectedMinute,
                onHourSelected = { hour ->
                    onStateChange(state.copy(selectedHour = hour))
                },
                onMinuteSelected = { minute ->
                    onStateChange(state.copy(selectedMinute = minute))
                }
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val selectedDate = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            onStateChange(state.copy(selectedDate = selectedDate))
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}
