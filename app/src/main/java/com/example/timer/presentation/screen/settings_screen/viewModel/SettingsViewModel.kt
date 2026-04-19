package com.example.timer.presentation.screen.settings_screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.domain.useCase.DeleteAllSequencesUseCase
import com.example.timer.domain.useCase.GetLanguageUseCase
import com.example.timer.domain.useCase.GetThemeUseCase
import com.example.timer.domain.useCase.SetLanguageUseCase
import com.example.timer.domain.useCase.SetThemeUseCase
import com.example.timer.presentation.screen.settings_screen.action.SettingsAction
import com.example.timer.presentation.screen.settings_screen.state.SettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val deleteAllSequence: DeleteAllSequencesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())

    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                selectedTheme = getThemeUseCase().first(),
                selectedLanguage = getLanguageUseCase().first()
            ) }
        }
    }

    fun onAction(action: SettingsAction) {
        when(action) {
            is SettingsAction.ChangeLanguage -> {
                _state.update { it.copy(
                    selectedLanguage = action.language
                ) }

                viewModelScope.launch(Dispatchers.IO) {
                    setLanguageUseCase(language = action.language)
                }
            }

            is SettingsAction.ChangeTheme -> {
                _state.update { it.copy(
                    selectedTheme = action.theme
                ) }

                viewModelScope.launch(Dispatchers.IO) {
                    setThemeUseCase(theme = action.theme)
                }
            }

            SettingsAction.DeleteAllSequences -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteAllSequence()
                }
            }
        }
    }
}