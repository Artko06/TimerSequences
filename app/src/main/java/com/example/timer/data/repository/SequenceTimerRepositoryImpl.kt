package com.example.timer.data.repository

import com.example.timer.data.local.roomDB.dao.PhasesDao
import com.example.timer.data.local.roomDB.dao.SequencesDao
import com.example.timer.data.mappers.toDomain
import com.example.timer.data.mappers.toEntity
import com.example.timer.data.mappers.toPhaseEntities
import com.example.timer.domain.model.SequenceItem
import com.example.timer.domain.repository.SequenceTimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SequenceTimerRepositoryImpl(
    private val phaseDao: PhasesDao,
    private val sequenceDao: SequencesDao
): SequenceTimerRepository {
    override suspend fun getSequenceById(id: Long): SequenceItem? {
        return sequenceDao.getSequenceWithPhasesById(sequenceId = id)?.toDomain()
    }

    override fun getAllSequences(): Flow<List<SequenceItem>> {
        return sequenceDao.getAllSequencesWithPhases().map { listSequencesWithPhases ->
            listSequencesWithPhases.map { sequenceWithPhases ->
                sequenceWithPhases.toDomain()
            }
        }
    }

    override suspend fun upsertSequence(sequenceItem: SequenceItem): Long {
        val sequenceId = sequenceDao.upsertSequence(
            sequence = sequenceItem.toEntity()
        )

        if (sequenceItem.id != 0L) {
            phaseDao.deletePhasesBySeqId(sequenceId = sequenceId)
        }

        phaseDao.upsertPhases(
            phaseEntities = toPhaseEntities(
                sequenceId = sequenceId,
                phases = sequenceItem.phases
            )
        )

        return sequenceId
    }

    override suspend fun deleteSequenceById(sequenceId: Long) {
        sequenceDao.deleteSequenceById(sequenceId)
    }

    override suspend fun deleteAllSequences() {
        sequenceDao.deleteAllSequences()
    }
}