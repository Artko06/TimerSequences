package com.example.timer.domain.useCase

import com.example.timer.domain.model.SequenceItem
import com.example.timer.domain.repository.SequenceTimerRepository
import kotlinx.coroutines.flow.Flow

class GetAllSequencesUseCase(
    private val sequencesTimerRepository: SequenceTimerRepository
) {
    operator fun invoke(): Flow<List<SequenceItem>> {
        return sequencesTimerRepository.getAllSequences()
    }
}