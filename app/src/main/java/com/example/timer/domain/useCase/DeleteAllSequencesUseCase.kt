package com.example.timer.domain.useCase

import com.example.timer.domain.repository.SequenceTimerRepository

class DeleteAllSequencesUseCase(
    private val sequenceTimerRepository: SequenceTimerRepository
) {
    suspend operator fun invoke() {
        sequenceTimerRepository.deleteAllSequences()
    }
}