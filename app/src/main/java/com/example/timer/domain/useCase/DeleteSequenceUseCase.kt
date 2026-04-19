package com.example.timer.domain.useCase

import com.example.timer.domain.repository.SequenceTimerRepository

class DeleteSequenceUseCase(
    private val sequenceTimerRepository: SequenceTimerRepository
) {
    suspend operator fun invoke(sequenceId: Long) {
        sequenceTimerRepository.deleteSequenceById(sequenceId)
    }
}