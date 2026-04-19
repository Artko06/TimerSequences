package com.example.timer.presentation.service.state

data class TimerServiceState(
    val runningId: Long = 0L,
    val leftSeconds: Int = 0,
    val allSeconds: Int = 0,
    val isRunning: Boolean = false,
    val currentPhaseIndex: Int = 0,
    val currentRepeat: Int = 1,
    val currentPhaseType: String = "",
    val currentColor: Int = 0xFFFFFF,
    val isFinished: Boolean = true
)
