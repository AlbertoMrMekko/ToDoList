package com.albertomrmekko.todolist.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.albertomrmekko.todolist.domain.model.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemePreferencesDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore

    private val THEME_KEY = stringPreferencesKey("app_theme")

    val theme: Flow<AppTheme> =
        dataStore.data.map { prefs ->
            AppTheme.valueOf(prefs[THEME_KEY] ?: AppTheme.DARK.name)
        }

    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { prefs -> prefs[THEME_KEY] = theme.name }
    }
}