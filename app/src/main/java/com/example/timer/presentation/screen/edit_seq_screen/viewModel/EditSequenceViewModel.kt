package com.example.timer.presentation.screen.edit_seq_screen.viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.domain.model.PhaseItem
import com.example.timer.domain.model.PhaseType
import com.example.timer.domain.useCase.DeleteSequenceUseCase
import com.example.timer.domain.useCase.GetSequenceByIdUseCase
import com.example.timer.domain.useCase.InsertSequenceUseCase
import com.example.timer.domain.useCase.UpdateSequenceUseCase
import com.example.timer.presentation.screen.edit_seq_screen.action.EditSequenceAction
import com.example.timer.presentation.screen.edit_seq_screen.state.EditSeqState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditSequenceViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val deleteSequenceUseCase: DeleteSequenceUseCase,
    private val getSequenceByIdUseCase: GetSequenceByIdUseCase,
    private val updateSequenceUseCase: UpdateSequenceUseCase,
    private val insertSequenceUseCase: InsertSequenceUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(EditSeqState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val sequenceId = savedStateHandle.get<Long>("sequenceId")

            val getSequenceById =
                if (sequenceId != null) getSequenceByIdUseCase(sequenceId) else null

            getSequenceById?.let { seq ->
                _state.update {
                    it.copy(
                        sequenceId = seq.id,
                        seqName = seq.seqName,
                        color = Color(seq.color),
                        phases = seq.phases,
                        repeatCount = seq.repeatCount
                    )
                }
            }
        }
    }

    fun onAction(action: EditSequenceAction) {
        when (action) {
            is EditSequenceAction.AddPhase -> {
                val currentPhases = _state.value.phases
                val lastType = currentPhases.lastOrNull()?.type
                val nextType = when (lastType) {
                    PhaseType.WORK -> PhaseType.REST
                    else -> PhaseType.WORK
                }

                val newPhase = PhaseItem(
                    id = 0,
                    type = nextType,
                    durationPhase = action.durationSeconds,
                )
                _state.update { it.copy(phases = currentPhases + newPhase) }
            }

            EditSequenceAction.DeleteSequence -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val currentSeqId = _state.value.sequenceId
                    if (currentSeqId != null) {
                        deleteSequenceUseCase(sequenceId = currentSeqId)
                    }
                }
            }

            is EditSequenceAction.MovePhaseDown -> {
                if (action.index < _state.value.phases.size - 1) {
                    val updatedPhases = _state.value.phases.toMutableList()
                    updatedPhases.swap(action.index, action.index + 1)

                    _state.update { it.copy(phases = updatedPhases) }
                }
            }

            is EditSequenceAction.MovePhaseUp -> {
                if (action.index > 0) {
                    val updatedPhases = _state.value.phases.toMutableList()
                    updatedPhases.swap(action.index, action.index - 1)

                    _state.update { it.copy(phases = updatedPhases) }
                }
            }

            is EditSequenceAction.RemovePhase -> {
                if (_state.value.phases.size > 1) {
                    val updatedPhases = _state.value.phases.toMutableList()
                    updatedPhases.removeAt(action.index)

                    _state.update { it.copy(phases = updatedPhases) }
                }
            }

            EditSequenceAction.SaveSequence -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (_state.value.seqName.isBlank()) {
                        _state.update { it.copy(
                            seqName = "${System.currentTimeMillis() % 1000}"
                        ) }
                    }

                    if (_state.value.phases.isEmpty()) {
                        return@launch
                    }

                    val currentState = _state.value

                    if (currentState.sequenceId == null) {
                        // (INSERT)
                        insertSequenceUseCase.invoke(
                            seqName = currentState.seqName,
                            color = currentState.color.toArgb(),
                            phases = currentState.phases,
                            repeatCount = currentState.repeatCount
                        )
                    } else {
                        // (UPDATE)
                        updateSequenceUseCase.invoke(
                            id = currentState.sequenceId,
                            seqName = currentState.seqName,
                            color = currentState.color.toArgb(),
                            phases = currentState.phases,
                            repeatCount = currentState.repeatCount
                        )
                    }
                }
            }

            is EditSequenceAction.UpdateColor -> {
                _state.update { it.copy(color = action.color) }
            }

            is EditSequenceAction.UpdateName -> {
                _state.update { it.copy(seqName = action.name) }
            }

            is EditSequenceAction.UpdatePhase -> {
                val updatedPhases = _state.value.phases.toMutableList()
                updatedPhases[action.index] = updatedPhases[action.index].copy(
                    type = action.type,
                    durationPhase = action.durationSeconds
                )

                _state.update { it.copy(phases = updatedPhases) }
            }

            is EditSequenceAction.UpdateRepeatCount -> {
                _state.update { it.copy(repeatCount = action.count) }
            }

        }
    }

    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }
}