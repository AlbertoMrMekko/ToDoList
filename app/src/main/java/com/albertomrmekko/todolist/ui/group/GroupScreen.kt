package com.albertomrmekko.todolist.ui.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.albertomrmekko.todolist.data.local.entity.GroupEntity
import com.albertomrmekko.todolist.domain.model.AppTheme
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