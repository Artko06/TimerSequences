package com.example.timer.domain.useCase

import com.example.timer.domain.model.ThemeApp
import com.example.timer.domain.repository.SettingsRepository

class SetThemeUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(theme: ThemeApp) = repository.setTheme(theme = theme)
}