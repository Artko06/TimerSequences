package com.example.timer.di

import com.example.timer.data.local.roomDB.dao.PhasesDao
import com.example.timer.data.local.roomDB.dao.SequencesDao
import com.example.timer.data.repository.SequenceTimerRepositoryImpl
import com.example.timer.data.repository.SettingsRepositoryImpl
import com.example.timer.domain.manager.TimerManager
import com.example.timer.domain.repository.SequenceTimerRepository
import com.example.timer.domain.repository.SettingsRepository
import com.example.timer.presentation.service.manager.TimerManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule: Module = module {
    single<SequenceTimerRepositoryImpl> {
        SequenceTimerRepositoryImpl(
            phaseDao = get<PhasesDao>(),
            sequenceDao = get<SequencesDao>()
        )
    }.bind<SequenceTimerRepository>()

    single<SettingsRepositoryImpl> {
        SettingsRepositoryImpl(
            context = androidContext()
        )
    }.bind<SettingsRepository>()

    single<TimerManagerImpl> {
        TimerManagerImpl(
            context = androidContext()
        )
    }.bind<TimerManager>()
}