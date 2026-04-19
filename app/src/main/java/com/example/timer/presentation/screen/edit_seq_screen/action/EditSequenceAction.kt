package com.example.timer.presentation.screen.edit_seq_screen.action


import androidx.compose.ui.graphics.Color
import com.example.timer.domain.model.PhaseType

sealed class EditSequenceAction {
    data class UpdateName(val name: String) : EditSequenceAction()
    data class UpdateColor(val color: Color) : EditSequenceAction()
    data class AddPhase(val durationSeconds: Int) : EditSequenceAction()
    data class RemovePhase(val index: Int) : EditSequenceAction()
    data class UpdatePhase(val index: Int, val type: PhaseType, val durationSeconds: Int) : EditSequenceAction()
    data class MovePhaseUp(val index: Int) : EditSequenceAction()
    data class MovePhaseDown(val index: Int) : EditSequenceAction()
    data class UpdateRepeatCount(val count: Int) : EditSequenceAction()
    data object SaveSequence : EditSequenceAction()
    data object DeleteSequence : EditSequenceAction()
}