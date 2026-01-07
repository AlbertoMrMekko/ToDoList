package com.albertomrmekko.todolist.domain.repository

import com.albertomrmekko.todolist.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val theme: Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
}