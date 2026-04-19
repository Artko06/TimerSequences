package com.example.timer.presentation.screen.settings_screen.state

import com.example.timer.domain.model.LanguageApp
import com.example.timer.domain.model.ThemeApp

data class SettingsState(
    val selectedLanguage: LanguageApp = LanguageApp.SYSTEM,
    val selectedTheme: ThemeApp = ThemeApp.SYSTEM
)
