package com.albertomrmekko.todolist.ui.group

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.ui.common.dialog.ConfirmDeleteDialog
import com.albertomrmekko.todolist.ui.common.dialog.GroupDialog
import com.albertomrmekko.todolist.ui.navigation.Screen

@Composable
fun GroupScreen(
    navController: NavController,
    viewModel: GroupViewModel = hiltViewModel()
) {
    val groups by viewModel.groups.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var groupToEdit by remember { mutableStateOf<GroupEntity?>(null) }
    var groupToDelete by remember { mutableStateOf<GroupEntity?>(null) }

    Scaffold(
        topBar = {
            GroupTopBar(
                isEditMode = isEditMode,
                onToggleEdit = { isEditMode = !isEditMode }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(
                items = groups,
                key = { group -> group.id }
            ) { group ->
                GroupItem(
                    group = group,
                    isEditMode = isEditMode,
                    onClick = {
                        navController.navigate(
                            Screen.TaskList.createRoute(group.id)
                        )
                    },
                    onEditClick = {
                        groupToEdit = group
                    },
                    onDeleteClick = {
                        groupToDelete = group
                    }
                )
            }

            if (!isEditMode) {
                item {
                    AddGroupItem(
                        onClick = {
                            showCreateDialog = true
                        }
                    )
                }
            }
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
        ConfirmDeleteDialog(
            title = "Eliminar grupo",
            message = "Se eliminará el grupo y todas sus tareas. ¿Deseas continuar?",
            confirmText = "Eliminar grupo",
            dismissText = "Cancelar",
            onConfirm = {
                viewModel.deleteGroup(group)
                groupToDelete = null
            },
            onDismiss = {
                groupToDelete = null
            }
        )
    }
}