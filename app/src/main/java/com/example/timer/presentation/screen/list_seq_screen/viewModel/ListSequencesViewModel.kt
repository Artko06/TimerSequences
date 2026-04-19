package com.example.timer.presentation.screen.list_seq_screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.domain.useCase.GetAllSequencesUseCase
import com.example.timer.presentation.screen.list_seq_screen.state.ListSeqState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ListSequencesViewModel(
    private val getAllSequencesUseCase: GetAllSequencesUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(ListSeqState())

    private val _observeSequences = getAllSequencesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val state = combine(
        _state,
        _observeSequences
    ) { state, observeSeq ->
        state.copy(
            listSequences = observeSeq
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ListSeqState()
        )
}