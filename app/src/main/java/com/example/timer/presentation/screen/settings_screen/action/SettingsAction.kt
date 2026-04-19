package com.example.timer.presentation.screen.settings_screen.action

import com.example.timer.domain.model.LanguageApp
import com.example.timer.domain.model.ThemeApp

sealed class SettingsAction {
    data class ChangeLanguage(val language: LanguageApp): SettingsAction()
    data class ChangeTheme(val theme: ThemeApp): SettingsAction()
    object DeleteAllSequences: SettingsAction()
}