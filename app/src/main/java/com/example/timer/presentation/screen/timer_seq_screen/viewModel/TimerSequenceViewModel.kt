package com.example.timer.presentation.screen.timer_seq_screen.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.domain.manager.TimerManager
import com.example.timer.domain.useCase.GetSequenceByIdUseCase
import com.example.timer.presentation.screen.timer_seq_screen.action.TimerSequenceAction
import com.example.timer.presentation.screen.timer_seq_screen.state.TimerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerSequenceViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getSequenceByIdUseCase: GetSequenceByIdUseCase,
    private val timerManager: TimerManager
): ViewModel() {
    private val _state = MutableStateFlow(TimerState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _timerServiceData = timerManager.binder
        .flatMapLatest { binder ->
            binder?.getService()?.state ?: flowOf(null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val state = combine(
        _state,
        _timerServiceData
    ) { state, timerServiceData ->
        val isSameSequence = timerServiceData?.runningId == state.sequence?.id

        if (timerServiceData != null && isSameSequence && !timerServiceData.isFinished) {
            state.copy(
                timeLeftSeconds = timerServiceData.leftSeconds,
                isRunning = timerServiceData.isRunning,
                currentRepeat = timerServiceData.currentRepeat,
                currentPhaseIndex = timerServiceData.currentPhaseIndex,
                currentAllSeconds = timerServiceData.allSeconds,
                isFinished = false
            )
        } else state
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TimerState()
        )

    init {
        timerManager.bind()

        viewModelScope.launch(Dispatchers.IO) {
            val sequenceId = savedStateHandle.get<Long>("sequenceId")

            val getSequenceById =
                if (sequenceId != null) getSequenceByIdUseCase(sequenceId) else null

            getSequenceById?.let { seq ->
                _state.update {
                    it.copy(
                        sequence = seq,
                        timeLeftSeconds = seq.phases.firstOrNull()?.durationPhase ?: 0
                    )
                }
            }
        }
    }

    fun onAction(action: TimerSequenceAction) {
        when(action) {
            TimerSequenceAction.ToggleTimer -> toggleTimer()
            TimerSequenceAction.MoveToNextPhase -> timerManager.moveToNext()
            TimerSequenceAction.MoveToPreviousPhase -> timerManager.moveToPrevious()
            TimerSequenceAction.StopTimer -> stopTimer()
        }
    }

    private fun stopTimer() {
        timerManager.stop()

        val sequence = _state.value.sequence
        _state.update {
            it.copy(
                timeLeftSeconds = sequence?.phases?.firstOrNull()?.durationPhase ?: 0,
                isRunning = false,
                currentPhaseIndex = 0,
                currentRepeat = 1,
                isFinished = false
            )
        }
    }

    private fun toggleTimer() {
        val serviceState = _timerServiceData.value

        if (serviceState == null || serviceState.isFinished) {
            runTimer()
        } else {
            timerManager.toggleResumePause()
        }
    }

    private fun runTimer() {
        val sequence = _state.value.sequence ?: return
        timerManager.bind()
        timerManager.start(sequence.id)
    }

    override fun onCleared() {
        super.onCleared()
        timerManager.unbind()
    }
}