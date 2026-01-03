package com.albertomrmekko.todolist.ui.group

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTopBar(
    isEditMode: Boolean,
    onToggleEdit: () -> Unit
) {
    TopAppBar(
        title = { Text("Grupos") },
        actions = {
            TextButton(onClick = onToggleEdit) {
                Text(if (isEditMode) "OK" else "Editar")
            }
        }
    )
}