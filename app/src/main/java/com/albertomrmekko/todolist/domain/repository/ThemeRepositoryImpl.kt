package com.albertomrmekko.todolist.domain.repository

import com.albertomrmekko.todolist.data.preferences.ThemePreferencesDataSource
import com.albertomrmekko.todolist.domain.model.AppTheme
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val dataSource: ThemePreferencesDataSource
) : ThemeRepository {
    override val theme = dataSource.theme

    override suspend fun setTheme(theme: AppTheme) {
        dataSource.setTheme(theme)
    }
}