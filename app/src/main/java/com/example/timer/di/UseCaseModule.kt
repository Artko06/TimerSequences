package com.example.timer.di

import com.example.timer.domain.repository.SequenceTimerRepository
import com.example.timer.domain.repository.SettingsRepository
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
import org.koin.core.module.Module
import org.koin.dsl.module

val useCaseModule: Module = module {
    factory<GetAllSequencesUseCase> {
        GetAllSequencesUseCase(
            sequencesTimerRepository = get<SequenceTimerRepository>()
        )
    }

    factory<GetSequenceByIdUseCase> {
        GetSequenceByIdUseCase(
            sequenceTimerRepository = get<SequenceTimerRepository>()
        )
    }

    factory<InsertSequenceUseCase> {
        InsertSequenceUseCase(
            sequenceTimerRepository = get<SequenceTimerRepository>()
        )
    }

    factory<UpdateSequenceUseCase> {
        UpdateSequenceUseCase(
            sequenceTimerRepository = get<SequenceTimerRepository>()
        )
    }

    factory<DeleteAllSequencesUseCase> {
        DeleteAllSequencesUseCase(
            sequenceTimerRepository = get<SequenceTimerRepository>()
        )
    }

    factory<DeleteSequenceUseCase> {
        DeleteSequenceUseCase(
            sequenceTimerRepository = get<SequenceTimerRepository>()
        )
    }


    factory<GetThemeUseCase> {
        GetThemeUseCase(
            repository = get<SettingsRepository>()
        )
    }

    factory<SetThemeUseCase> {
        SetThemeUseCase(
            repository = get<SettingsRepository>()
        )
    }

    factory<GetLanguageUseCase> {
        GetLanguageUseCase(
            repository = get<SettingsRepository>()
        )
    }

    factory<SetLanguageUseCase> {
        SetLanguageUseCase(
            repository = get<SettingsRepository>()
        )
    }
}