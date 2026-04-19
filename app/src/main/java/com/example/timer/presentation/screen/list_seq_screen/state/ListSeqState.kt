package com.example.timer.presentation.screen.list_seq_screen.state

import com.example.timer.domain.model.SequenceItem

data class ListSeqState(
    val listSequences: List<SequenceItem> = emptyList(),
)
