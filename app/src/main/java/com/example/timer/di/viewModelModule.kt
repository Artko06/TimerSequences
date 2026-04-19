package com.example.timer.di

import androidx.lifecycle.SavedStateHandle
import com.example.timer.MainActivityViewModel
import com.example.timer.domain.manager.TimerManager
import com.example.timer.domain.useCase.DeleteAllSequencesUseCase
import com.example.timer.domain.useCase.DeleteSequenceUseCase
import com.example.timer.domain.useCase.GetAllSequencesUseCase
import com.example.timer.domain.useCase.GetLanguageUseCase
import com.example.timer.domain.useCase.GetSequenceByIdUseCase
import com.example.timer.domain.useCase.GetThemeUseCase
import com.example.timer.domain.useCase.InsertSequenceUseCase
import com.example.timer.domain.useCase.SetLanguageUseCase
import com.example.timer.domain.useCase.SetThemeUseCase
import com.example.timer.domain.useCase.UpdateSequenceUseCase
import com.example.timer.presentation.screen.edit_seq_screen.viewModel.EditSequenceViewModel
import com.example.timer.presentation.screen.list_seq_screen.viewModel.ListSequencesViewModel
import com.example.timer.presentation.screen.settings_screen.viewModel.SettingsViewModel
import com.example.timer.presentation.screen.timer_seq_screen.viewModel.TimerSequenceViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule: Module = module {
    viewModel<ListSequencesViewModel> { ListSequencesViewModel(
        getAllSequencesUseCase = get<GetAllSequencesUseCase>()
    ) }

    viewModel<TimerSequenceViewModel> { (handle: SavedStateHandle) ->
        TimerSequenceViewModel(
            savedStateHandle = handle,
            getSequenceByIdUseCase = get<GetSequenceByIdUseCase>(),
            timerManager = get<TimerManager>()
        )
    }

    viewModel<EditSequenceViewModel> { (handle: SavedStateHandle) -> EditSequenceViewModel(
        savedStateHandle = handle,
        deleteSequenceUseCase = get<DeleteSequenceUseCase>(),
        getSequenceByIdUseCase = get<GetSequenceByIdUseCase>(),
        updateSequenceUseCase = get<UpdateSequenceUseCase>(),
        insertSequenceUseCase = get<InsertSequenceUseCase>()
    ) }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            getLanguageUseCase = get<GetLanguageUseCase>(),
            setLanguageUseCase = get<SetLanguageUseCase>(),
            getThemeUseCase = get<GetThemeUseCase>(),
            setThemeUseCase = get<SetThemeUseCase>(),
            deleteAllSequence = get<DeleteAllSequencesUseCase>()
        )
    }

    viewModel<MainActivityViewModel> {
        MainActivityViewModel(
            getThemeUseCase = get<GetThemeUseCase>(),
            getLanguageUseCase = get<GetLanguageUseCase>()
        )
    }
}