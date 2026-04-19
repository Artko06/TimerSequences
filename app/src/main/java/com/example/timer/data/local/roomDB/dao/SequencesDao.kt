package com.example.timer.data.local.roomDB.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.timer.data.local.roomDB.entity.SequenceEntity
import com.example.timer.data.local.roomDB.entity.relation.SequenceWithPhases
import kotlinx.coroutines.flow.Flow

@Dao
interface SequencesDao {
    @Query("SELECT * FROM SEQUENCES")
    fun getAllSequences(): Flow<List<SequenceEntity>>

    @Query("SELECT * FROM SEQUENCES WHERE sequence_id = :sequenceId")
    suspend fun getSequenceById(sequenceId: Long) : SequenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSequence(sequence: SequenceEntity): Long

    @Query("DELETE FROM SEQUENCES WHERE sequence_id = :sequenceId")
    suspend fun deleteSequenceById(sequenceId: Long)

    @Query("DELETE FROM SEQUENCES")
    suspend fun deleteAllSequences()

    @Transaction
    @Query("SELECT * FROM SEQUENCES")
    fun getAllSequencesWithPhases(): Flow<List<SequenceWithPhases>>

    @Transaction
    @Query("SELECT * FROM SEQUENCES WHERE sequence_id = :sequenceId")
    suspend fun getSequenceWithPhasesById(sequenceId: Long): SequenceWithPhases?
}