package com.example.timer.domain.useCase

import com.example.timer.domain.model.LanguageApp
import com.example.timer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetLanguageUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<LanguageApp> = repository.getLanguage()
}