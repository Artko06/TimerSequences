package com.example.timer.domain.useCase

import com.example.timer.domain.model.SequenceItem
import com.example.timer.domain.repository.SequenceTimerRepository

class GetSequenceByIdUseCase(
    private val sequenceTimerRepository: SequenceTimerRepository
) {
    suspend operator fun invoke(sequenceId: Long): SequenceItem? {
        return sequenceTimerRepository.getSequenceById(id = sequenceId)
    }
}