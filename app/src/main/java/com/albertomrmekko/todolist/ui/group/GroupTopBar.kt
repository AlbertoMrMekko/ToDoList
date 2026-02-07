package com.albertomrmekko.todolist.ui.group

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.albertomrmekko.todolist.domain.model.AppTheme

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
