package com.example.timer.data.local.roomDB.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.timer.data.local.roomDB.entity.PhaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhasesDao {
    @Query("SELECT * FROM PHASES WHERE sequence_id = :sequenceId")
    fun getPhasesBySequenceId(sequenceId: Long): Flow<List<PhaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPhases(phaseEntities: List<PhaseEntity>): List<Long>

    @Query("DELETE FROM PHASES WHERE sequence_id = :sequenceId")
    suspend fun deletePhasesBySeqId(sequenceId: Long)

    @Delete
    suspend fun deletePhase(phaseEntity: PhaseEntity)
}