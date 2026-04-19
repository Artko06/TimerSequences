package com.example.timer.di


import androidx.room.Room
import com.example.timer.data.local.roomDB.TimerDatabase
import com.example.timer.data.local.roomDB.dao.PhasesDao
import com.example.timer.data.local.roomDB.dao.SequencesDao
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {
    single<TimerDatabase> {
        Room.databaseBuilder(
            context = androidContext(),
            TimerDatabase::class.java,
            TimerDatabase.DATABASE_NAME
        ).build()
    }

    single<PhasesDao> {
        get<TimerDatabase>().phasesDao()
    }

    single<SequencesDao> {
        get<TimerDatabase>().sequencesDao()
    }
}