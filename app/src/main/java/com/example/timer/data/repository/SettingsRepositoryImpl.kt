package com.example.timer.data.repository

import android.content.Context
import com.example.timer.data.local.preferences.PreferencesDataStore
import com.example.timer.domain.model.LanguageApp
import com.example.timer.domain.model.ThemeApp
import com.example.timer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val context: Context
): SettingsRepository {
    override fun getLanguage(): Flow<LanguageApp> {
        return PreferencesDataStore.getLanguage(context = context)
    }

    override suspend fun saveLanguage(language: LanguageApp) {
        PreferencesDataStore.setLanguage(context = context, language = language)
    }

    override fun getTheme(): Flow<ThemeApp> {
        return PreferencesDataStore.getTheme(context = context)
    }

    override suspend fun setTheme(theme: ThemeApp) {
        PreferencesDataStore.setTheme(context = context, theme = theme)
    }
}