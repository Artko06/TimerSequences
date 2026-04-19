package com.example.timer.presentation.screen.edit_seq_screen.state

import androidx.compose.ui.graphics.Color
import com.example.timer.domain.model.PhaseItem

data class EditSeqState(
    val sequenceId: Long? = null,
    val seqName: String = "",
    val color: Color = Color.Blue,
    val phases: List<PhaseItem> = emptyList(),
    val repeatCount: Int = 1,
)