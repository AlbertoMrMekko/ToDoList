package com.albertomrmekko.todolist.ui.group

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.domain.model.AppTheme
import com.albertomrmekko.todolist.domain.model.GroupColor
import com.albertomrmekko.todolist.ui.apptheme.AppViewModel
import com.albertomrmekko.todolist.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    navController: NavController,
    viewModel: GroupViewModel = hiltViewModel(),
    appViewModel: AppViewModel = hiltViewModel()
) {
    val groups by viewModel.groups.collectAsState()
    val theme by appViewModel.theme.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var groupToEdit by remember { mutableStateOf<GroupEntity?>(null) }
    var groupToDelete by remember { mutableStateOf<GroupEntity?>(null) }

    Scaffold(
        topBar = {
            GroupTopBar(
                theme = theme,
                isEditMode = isEditMode,
                onThemeChange = {
                    appViewModel.setTheme(if (it) AppTheme.DARK else AppTheme.LIGHT)
                }
            )
        },
        bottomBar = {
            GroupBottomBar(
                isEditMode = isEditMode,
                onCreateClick = { showCreateDialog = true },
                onEditToggle = { isEditMode = !isEditMode }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "MIS GRUPOS",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            GroupListBox(
                groups = groups,
                isEditMode = isEditMode,
                onGroupClick = { group ->
                    if (!isEditMode) {
                        navController.navigate(Screen.TaskList.createRoute(group.id))
                    }
                },
                onEditClick = { groupToEdit = it },
                onDeleteClick = { groupToDelete = it }
            )
        }
    }

    if (showCreateDialog) {
        GroupDialog(
            title = "Nuevo grupo",
            confirmText = "Crear",
            onConfirm = { name, color ->
                viewModel.addGroup(name, color)
                showCreateDialog = false
            },
            onDismiss = {
                showCreateDialog = false
            }
        )
    }

    groupToEdit?.let { group ->
        GroupDialog(
            title = "Editar grupo",
            initialName = group.name,
            initialColor = group.color,
            confirmText = "Guardar",
            onConfirm = { newName, newColor ->
                viewModel.updateGroup(group.copy(name = newName, color = newColor))
                groupToEdit = null
            },
            onDismiss = {
                groupToEdit = null
            }
        )
    }

    groupToDelete?.let { group ->
        AlertDialog(
            onDismissRequest = { groupToDelete = null },
            title = { Text("Eliminar grupo") },
            text = { Text("Se eliminará el grupo y todas sus tareas. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteGroup(group)
                    groupToDelete = null
                }) {
                    Text("Eliminar grupo")
                }
            },
            dismissButton = {
                TextButton(onClick = { groupToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTopBar(
    theme: AppTheme,
    isEditMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    TopAppBar(
        title = { Text("RECORDATORIOS") },
        actions = {
            Switch(
                checked = theme == AppTheme.DARK,
                enabled = !isEditMode,
                onCheckedChange = onThemeChange
            )
        }
    )
}

@Composable
fun GroupListBox(
    groups: List<GroupEntity>,
    isEditMode: Boolean,
    onGroupClick: (GroupEntity) -> Unit,
    onEditClick: (GroupEntity) -> Unit,
    onDeleteClick: (GroupEntity) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            groups.forEach { group ->
                GroupRow(
                    group = group,
                    isEditMode = isEditMode,
                    onClick = { onGroupClick(group) },
                    onEditClick = { onEditClick(group) },
                    onDeleteClick = { onDeleteClick(group) }
                )
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}

@Composable
private fun GroupBottomBar(
    isEditMode: Boolean,
    onCreateClick: () -> Unit,
    onEditToggle: () -> Unit
) {
    Surface(tonalElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!isEditMode) {
                TextButton(onClick = onCreateClick) {
                    Icon(Icons.Default.AddCircle, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Nuevo grupo")
                }

                TextButton(onClick = onEditToggle) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Editar grupos")
                }
            } else {
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onEditToggle) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun GroupRow(
    group: GroupEntity,
    isEditMode: Boolean,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isEditMode) { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(group.color.toColor())
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = group.name,
            modifier = Modifier.weight(1f)
        )

        if (isEditMode) {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.RemoveCircle,
                    tint = Color.Red,
                    contentDescription = null
                )
            }
        } else {
            Text("3") // TODO num de tareas del grupo (no implementado)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
private fun GroupDialog(
    title: String,
    confirmText: String,
    initialName: String = "",
    initialColor: GroupColor = GroupColor.YELLOW,
    onConfirm: (String, GroupColor) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember(initialName) { mutableStateOf(initialName) }
    var selectedColor by remember(initialColor) { mutableStateOf(initialColor) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del grupo") }
                )

                Spacer(Modifier.height(16.dp))
                Text("Color")

                Spacer(Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GroupColor.entries.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color.toColor())
                                .border(
                                    width = if (color == selectedColor) 3.dp else 1.dp,
                                    color = if (color == selectedColor) Color.Black else Color.Gray,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onConfirm(name.trim(), selectedColor) }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}