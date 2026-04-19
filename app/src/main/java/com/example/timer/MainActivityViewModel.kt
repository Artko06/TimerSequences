package com.example.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.domain.model.LanguageApp
import com.example.timer.domain.model.ThemeApp
import com.example.timer.domain.useCase.GetLanguageUseCase
import com.example.timer.domain.useCase.GetThemeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
): ViewModel() {

    val themeType: StateFlow<ThemeApp> = getThemeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeApp.SYSTEM
        )

    val languageType: StateFlow<LanguageApp> = getLanguageUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LanguageApp.SYSTEM
        )
}