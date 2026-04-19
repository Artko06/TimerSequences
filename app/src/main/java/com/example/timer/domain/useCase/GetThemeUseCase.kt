package com.example.timer.domain.useCase

import com.example.timer.domain.model.ThemeApp
import com.example.timer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<ThemeApp> = repository.getTheme()
}