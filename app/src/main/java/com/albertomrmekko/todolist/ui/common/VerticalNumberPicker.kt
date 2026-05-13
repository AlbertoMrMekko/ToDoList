package com.albertomrmekko.todolist.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerticalNumberPicker(
    valueRange: IntRange,
    selectedValue: Int,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedValue)

    LaunchedEffect(selectedValue) {
        listState.animateScrollToItem(selectedValue)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(valueRange.toList()) { value ->
            val selected = value == selectedValue

            Text(
                text = value.toString().padStart(2, '0'),
                fontSize = if (selected) 28.sp else 20.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onValueSelected(value) }
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
