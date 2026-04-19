package com.example.timer

import android.app.Application
import com.example.timer.di.databaseModule
import com.example.timer.di.repositoryModule
import com.example.timer.di.useCaseModule
import com.example.timer.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TimerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TimerApplication)

            modules(
                databaseModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}