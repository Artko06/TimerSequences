package com.example.timer.presentation.screen.timer_seq_screen.state

import com.example.timer.domain.model.SequenceItem

data class TimerState(
    val sequence: SequenceItem? = null,
    val currentAllSeconds: Int = 0,
    val currentPhaseIndex: Int = 0,
    val currentRepeat: Int = 1,
    val timeLeftSeconds: Int = 0,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false
)
