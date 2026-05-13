package com.albertomrmekko.todolist.ui.common

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
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
    val itemHeight = 40.dp
    val visibleItemsCount = 3
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = (selectedValue - 1).coerceAtLeast(0)
    )

    LaunchedEffect(selectedValue) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
        }.collect { visibleItems ->
            if (visibleItems.size < 2)
                return@collect

            val centeredItem = visibleItems[1]
            val value = valueRange.first + centeredItem.index
            if (value in valueRange && value != selectedValue) {
                onValueSelected(value)
            }
        }
    }

    Box(
        modifier = modifier.height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = itemHeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = rememberSnapFlingBehavior(
                lazyListState = listState
            )
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
                        .height(itemHeight)
                        .wrapContentHeight(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
