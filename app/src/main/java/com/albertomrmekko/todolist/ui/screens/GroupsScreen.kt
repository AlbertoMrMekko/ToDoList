package com.albertomrmekko.todolist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albertomrmekko.todolist.ui.viewmodel.GroupsViewModel

@Composable
fun GroupsScreen(
    viewModel: GroupsViewModel = hiltViewModel()
) {
    val groups by viewModel.groups.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            viewModel.addGroup("Group ${groups.size + 1}")
        }) {
            Text("Add group")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(groups) { group ->
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}