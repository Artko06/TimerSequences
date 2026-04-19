package com.example.timer.domain.manager

import com.example.timer.presentation.service.TimerService
import kotlinx.coroutines.flow.StateFlow

interface TimerManager {
    val binder: StateFlow<TimerService.TimerBinder?>
    fun start(sequenceId: Long)
    fun toggleResumePause()
    fun moveToNext()
    fun moveToPrevious()
    fun stop()
    fun bind()
    fun unbind()
}