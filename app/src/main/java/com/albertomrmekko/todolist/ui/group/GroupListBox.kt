package com.albertomrmekko.todolist.ui.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.albertomrmekko.todolist.data.local.entity.GroupEntity

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
