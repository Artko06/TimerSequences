package com.example.timer.domain.repository

import com.example.timer.domain.model.LanguageApp
import com.example.timer.domain.model.ThemeApp
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    // Language
    fun getLanguage(): Flow<LanguageApp>
    suspend fun saveLanguage(language: LanguageApp)

    // Theme
    fun getTheme(): Flow<ThemeApp>
    suspend fun setTheme(theme: ThemeApp)
}