package com.example.timer.domain.repository

import com.example.timer.domain.model.SequenceItem
import kotlinx.coroutines.flow.Flow

interface SequenceTimerRepository {
    suspend fun getSequenceById(id: Long): SequenceItem?

    fun getAllSequences(): Flow<List<SequenceItem>>

    suspend fun upsertSequence(sequenceItem: SequenceItem): Long

    suspend fun deleteSequenceById(sequenceId: Long)

    suspend fun deleteAllSequences()
}