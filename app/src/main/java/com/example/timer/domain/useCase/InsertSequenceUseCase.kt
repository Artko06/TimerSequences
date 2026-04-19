package com.example.timer.domain.useCase

import com.example.timer.domain.model.PhaseItem
import com.example.timer.domain.model.SequenceItem
import com.example.timer.domain.repository.SequenceTimerRepository

class InsertSequenceUseCase(
    private val sequenceTimerRepository: SequenceTimerRepository
) {
    suspend operator fun invoke(
        seqName: String,
        color: Int,
        phases: List<PhaseItem>,
        repeatCount: Int = 1
    ): Long {
        return sequenceTimerRepository.upsertSequence(
            sequenceItem = SequenceItem(
                id = 0,
                seqName = seqName,
                color = color,
                phases = phases,
                repeatCount = repeatCount,
            )
        )
    }
}