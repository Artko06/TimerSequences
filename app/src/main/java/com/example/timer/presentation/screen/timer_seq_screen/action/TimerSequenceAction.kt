package com.example.timer.presentation.screen.timer_seq_screen.action

sealed class TimerSequenceAction {
    object ToggleTimer: TimerSequenceAction()
    object MoveToNextPhase: TimerSequenceAction()
    object MoveToPreviousPhase: TimerSequenceAction()
    object StopTimer : TimerSequenceAction()
}