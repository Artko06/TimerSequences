package com.example.timer.domain.useCase

import com.example.timer.domain.model.LanguageApp
import com.example.timer.domain.repository.SettingsRepository

class SetLanguageUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(language: LanguageApp) = repository.saveLanguage(language = language)
}