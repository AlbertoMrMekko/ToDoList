package com.albertomrmekko.todolist.ui.task

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTaskBar(
    onAdd: (String) -> Unit
) {
    var msg by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        TextField(
            value = msg,
            onValueChange = { msg = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Nueva tarea") }
        )

        IconButton(
            onClick = {
                if (msg.isNotBlank()) {
                    onAdd(msg)
                    msg = ""
                }
            }
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}