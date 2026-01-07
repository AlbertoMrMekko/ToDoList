package com.albertomrmekko.todolist.ui.apptheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertomrmekko.todolist.domain.model.AppTheme
import com.albertomrmekko.todolist.domain.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {
    val theme: StateFlow<AppTheme> = themeRepository.theme.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AppTheme.DARK
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeRepository.setTheme(theme)
        }
    }
}