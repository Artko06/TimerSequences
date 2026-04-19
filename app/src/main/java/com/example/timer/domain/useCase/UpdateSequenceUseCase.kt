package com.example.timer.domain.useCase

import com.example.timer.domain.model.PhaseItem
import com.example.timer.domain.model.SequenceItem
import com.example.timer.domain.repository.SequenceTimerRepository

class UpdateSequenceUseCase(
    private val sequenceTimerRepository: SequenceTimerRepository
) {
    suspend operator fun invoke(
        id: Long,
        seqName: String,
        color: Int,
        phases: List<PhaseItem>,
        repeatCount: Int,
    ): Long {
        return sequenceTimerRepository.upsertSequence(
            sequenceItem = SequenceItem(
                id = id,
                seqName = seqName,
                color = color,
                phases = phases,
                repeatCount = repeatCount,
            )
        )
    }
}