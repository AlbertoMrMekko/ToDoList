package com.albertomrmekko.todolist.ui.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.albertomrmekko.todolist.ui.group.GroupHeader

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val group by viewModel.group.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    Column {
        group?.let {
            GroupHeader(
                name = it.name,
                color = it.color
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = tasks,
                key = { task -> task.id }
            ) { task ->
                TaskItem(
                    task = task,
                    onDelete = { viewModel.deleteTask(task) },
                    onEdit = { newMessage ->
                        viewModel.updateTask(task.copy(message = newMessage))
                    }
                )
            }
        }

        AddTaskBar(
            onAdd = { message ->
                viewModel.addTask(message)
            }
        )
    }
}