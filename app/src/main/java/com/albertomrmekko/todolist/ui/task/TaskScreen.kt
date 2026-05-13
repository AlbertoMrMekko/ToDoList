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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.albertomrmekko.todolist.ui.common.TaskDateTimeSection
import com.albertomrmekko.todolist.ui.group.toColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            onConfirm = { message, date ->
                viewModel.addTask(message, date)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    taskToEdit?.let { task ->
        TaskDialog(
            title = "Editar tarea",
            confirmText = "Guardar",
            initialMessage = task.message,
            initialDate = task.date,
            onConfirm = { message, date ->
                viewModel.updateTask(
                    task.copy(
                        message = message,
                        date = date
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

            val hasDate = task.date != null
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

                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")
                    val formattedDate = task.date.format(formatter)

                    Text(
                        text = formattedDate,
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
    initialDate: LocalDateTime? = null,
    onConfirm: (
        message: String,
        date: LocalDateTime?
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var message by remember(initialMessage) { mutableStateOf(initialMessage) }

    var dateTimeState by remember(initialDate) {
        mutableStateOf(
            if (initialDate != null) {
                TaskDateTimeUiState(
                    dateEnabled = true,
                    timeEnabled = true,
                    selectedDate = initialDate.toLocalDate(),
                    selectedHour = initialDate.hour,
                    selectedMinute = initialDate.minute
                )
            } else {
                TaskDateTimeUiState()
            }
        )
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

                Spacer(modifier = Modifier.height(16.dp))

                TaskDateTimeSection(
                    state = dateTimeState,
                    onStateChange = {
                        dateTimeState = it
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = message.isNotBlank(),
                onClick = {
                    onConfirm(message.trim(), dateTimeState.toLocalDateTime())
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
