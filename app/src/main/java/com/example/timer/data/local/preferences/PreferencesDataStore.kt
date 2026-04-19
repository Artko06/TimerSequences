package com.example.timer.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.timer.domain.model.LanguageApp
import com.example.timer.domain.model.ThemeApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesDataStore {
    // Languages
    fun getLanguage(context: Context): Flow<LanguageApp> {
        return context.dataStore.data.map { prefs ->
            prefs[LANGUAGE_KEY]?.let { LanguageApp.valueOf(it) } ?: LanguageApp.SYSTEM
        }
    }

    suspend fun setLanguage(context: Context, language: LanguageApp) {
        context.dataStore.edit { it[LANGUAGE_KEY] = language.name }
    }

    // Theme
    fun getTheme(context: Context): Flow<ThemeApp> {
        return context.dataStore.data.map { prefs ->
            prefs[THEME_KEY]?.let { ThemeApp.valueOf(it) } ?: ThemeApp.SYSTEM
        }
    }

    suspend fun setTheme(context: Context, theme: ThemeApp) {
        context.dataStore.edit { it[THEME_KEY] = theme.name }
    }
}