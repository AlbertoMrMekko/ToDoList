package com.albertomrmekko.todolist.ui.task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.data.local.entity.TaskEntity
import com.albertomrmekko.todolist.domain.model.GroupColor
import com.albertomrmekko.todolist.ui.group.toColor
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(
    group: GroupEntity,
    viewModel: TaskViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var isEditMode by remember { mutableStateOf(false) }
    var showCompleted by remember { mutableStateOf(true) }

    var showCreateDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }

    Scaffold(
        topBar = {
            TaskTopBar(
                groupName = group.name,
                groupColor = group.color.toColor(),
                isEditMode = isEditMode,
                showCompleted = showCompleted,
                onBack = onBack,
                onToggleShowCompleted = { showCompleted = !showCompleted }
            )
        },
        bottomBar = {
            TaskBottomBar(
                isEditMode = isEditMode,
                onAddTask = { showCreateDialog = true },
                onEnterEditMode = { isEditMode = true },
                onExitEditMode = { isEditMode = false }
            )
        }
    ) { padding ->
        TaskListContent(
            modifier = Modifier.padding(padding),
            activeTasks = uiState.activeTasks,
            completedTasks = uiState.completedTasks,
            groupColor = group.color,
            isEditMode = isEditMode,
            showCompleted = showCompleted,
            onToggleCompleted = viewModel::toggleCompleted,
            onEditTask = { taskToEdit = it },
            onDeleteTask = viewModel::deleteTask
        )
    }

    if (showCreateDialog) {
        TaskDialog(
            title = "Nueva tarea",
            confirmText = "Crear",
            onConfirm = { message, date, time ->
                viewModel.addTask(message, date, time)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    taskToEdit?.let { task ->
        val date = task.reminderDate?.let { LocalDate.parse(it) }
        val time = task.reminderTime?.let { LocalTime.parse(it) }
        TaskDialog(
            title = "Editar tarea",
            confirmText = "Guardar",
            initialMessage = task.message,
            initialDateEnabled = date != null,
            initialTimeEnabled = time != null,
            initialDate = date,
            initialTime = time,
            onConfirm = { message, date, time ->
                viewModel.updateTask(
                    task.copy(
                        message = message,
                        reminderDate = date.toString(),
                        reminderTime = time.toString()
                    )
                )
                taskToEdit = null
            },
            onDismiss = { taskToEdit = null }
        )
    }
}

@Composable
private fun TaskTopBar(
    groupName: String,
    groupColor: Color,
    isEditMode: Boolean,
    showCompleted: Boolean,
    onBack: () -> Unit,
    onToggleShowCompleted: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    enabled = !isEditMode,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = if (!isEditMode) Color.White else Color.Transparent,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White, RoundedCornerShape(12.dp))
                        .clickable { onToggleShowCompleted() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showCompleted)
                            "Ocultar completadas"
                        else
                            "Mostrar completadas",
                        color = Color.White
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(48.dp))
                Text(
                    text = groupName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    color = groupColor,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.width(32.dp))
            }
        }
    }
}

@Composable
private fun TaskBottomBar(
    isEditMode: Boolean,
    onAddTask: () -> Unit,
    onEnterEditMode: () -> Unit,
    onExitEditMode: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isEditMode) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White, RoundedCornerShape(12.dp))
                        .clickable { onAddTask() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text("Nueva tarea", color = Color.White)
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White, RoundedCornerShape(12.dp))
                        .clickable { onEnterEditMode() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text("Editar tareas", color = Color.White)
                }
            } else {
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White, RoundedCornerShape(12.dp))
                        .clickable { onExitEditMode() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("OK", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun TaskListContent(
    modifier: Modifier,
    activeTasks: List<TaskEntity>,
    completedTasks: List<TaskEntity>,
    groupColor: GroupColor,
    isEditMode: Boolean,
    showCompleted: Boolean,
    onToggleCompleted: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {

        items(activeTasks, key = { it.id }) { task ->
            TaskRow(
                task = task,
                groupColor = groupColor.toColor(),
                isEditMode = isEditMode,
                onToggleCompleted = { onToggleCompleted(task) },
                onEdit = { onEditTask(task) },
                onDelete = { onDeleteTask(task) }
            )
        }

        if (showCompleted && completedTasks.isNotEmpty()) {
            item {
                Text(
                    "Tareas completadas",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(completedTasks, key = { it.id }) { task ->
                TaskRow(
                    task = task,
                    groupColor = groupColor.toColor(),
                    isEditMode = isEditMode,
                    onToggleCompleted = { onToggleCompleted(task) },
                    onEdit = { onEditTask(task) },
                    onDelete = { onDeleteTask(task) }
                )
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: TaskEntity,
    groupColor: Color,
    isEditMode: Boolean,
    onToggleCompleted: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            IconButton(
                onClick = onToggleCompleted,
                modifier = Modifier.size(36.dp)
            )
            {
                Icon(
                    imageVector = if (task.completed)
                        Icons.Default.CheckCircle
                    else
                        Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (task.completed) groupColor else Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            val hasDate = task.reminderDate != null || task.reminderTime != null
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = if (hasDate) Arrangement.Top else Arrangement.Center
            ) {
                Text(
                    text = task.message,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (hasDate) {
                    Spacer(modifier = Modifier.height(4.dp))

                    val dateText = task.reminderDate ?: ""
                    val timeText = task.reminderTime ?: ""

                    Text(
                        text = listOf(dateText, timeText)
                            .filter { it.isNotBlank() }
                            .joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier
                    .width(96.dp)
                    .height(48.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onEdit,
                    enabled = isEditMode
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar tarea",
                        modifier = Modifier.size(26.dp),
                        tint = if (isEditMode) LocalContentColor.current else Color.Transparent
                    )
                }

                IconButton(
                    onClick = onDelete,
                    enabled = isEditMode
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircle,
                        contentDescription = "Eliminar tarea",
                        modifier = Modifier.size(26.dp),
                        tint = if (isEditMode) Color.Red else Color.Transparent
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDialog(
    title: String,
    confirmText: String,
    initialMessage: String = "",
    initialDateEnabled: Boolean = false,
    initialTimeEnabled: Boolean = false,
    initialDate: LocalDate? = null,
    initialTime: LocalTime? = null,
    onConfirm: (
        message: String,
        date: LocalDate?,
        time: LocalTime?
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var message by remember(initialMessage) { mutableStateOf(initialMessage) }

    var dateEnabled by remember(initialDateEnabled) { mutableStateOf(initialDateEnabled) }
    var timeEnabled by remember(initialTimeEnabled) { mutableStateOf(initialTimeEnabled) }

    var selectedDate by remember(initialDate) { mutableStateOf(initialDate) }
    var selectedTime by remember(initialTime) { mutableStateOf(initialTime) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun applyDefaultsIfNeeded() {
        if (timeEnabled && !dateEnabled) {
            dateEnabled = true
            selectedDate = selectedDate ?: LocalDate.now()
        }
        if (dateEnabled && !timeEnabled) {
            selectedTime = selectedTime ?: LocalTime.of(0, 0)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Mensaje") },
                    maxLines = 3
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Fecha", modifier = Modifier.weight(1f))
                    Switch(
                        checked = dateEnabled,
                        onCheckedChange = {
                            dateEnabled = it
                            if (dateEnabled && selectedDate == null) selectedDate = LocalDate.now()
                            applyDefaultsIfNeeded()
                        }
                    )
                }

                if (dateEnabled) {
                    Spacer(Modifier.height(8.dp))
                    DatePickerInline(
                        initialDate = selectedDate ?: LocalDate.now(),
                        onDateSelected = { selectedDate = it }
                    )
                }

                Spacer(Modifier.height(12.dp))

                // HORA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Hora", modifier = Modifier.weight(1f))
                    Switch(
                        checked = timeEnabled,
                        onCheckedChange = {
                            timeEnabled = it
                            if (timeEnabled && selectedTime == null) {
                                selectedTime = LocalTime.now().withSecond(0).withNano(0)
                            }
                            applyDefaultsIfNeeded()
                        }
                    )
                }

                if (timeEnabled) {
                    Spacer(Modifier.height(8.dp))
                    TimePickerInline(
                        initialTime = selectedTime ?: LocalTime.of(0, 0),
                        onTimeSelected = { selectedTime = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = message.isNotBlank(),
                onClick = {
                    applyDefaultsIfNeeded()

                    onConfirm(message.trim(), selectedDate, selectedTime)
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInline(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    DatePicker(
        state = state,
        showModeToggle = false,
        colors = DatePickerDefaults.colors()
    )

    val selectedMillis = state.selectedDateMillis ?: return

    val selectedDate = Instant.ofEpochMilli(selectedMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    onDateSelected(selectedDate)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerInline(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )

    TimePicker(state = state)

    onTimeSelected(LocalTime.of(state.hour, state.minute))
}
