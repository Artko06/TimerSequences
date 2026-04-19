package com.example.timer.domain.model

data class SequenceItem(
    val id: Long,
    val seqName: String,
    val color: Int,
    val phases: List<PhaseItem>,
    val repeatCount: Int,
)