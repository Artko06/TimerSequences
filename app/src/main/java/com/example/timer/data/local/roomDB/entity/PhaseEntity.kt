package com.example.timer.data.local.roomDB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "phases",
    foreignKeys = [
        ForeignKey(
            entity = SequenceEntity::class,
            parentColumns = ["sequence_id"],
            childColumns = ["sequence_id"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("sequence_id")
    ]
)
data class PhaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "phase_id")
    val id: Long = 0,

    @ColumnInfo(name = "sequence_id")
    val sequenceId: Long,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "duration_phase")
    val durationPhase: Int,

    @ColumnInfo(name = "number_phase")
    val numberPhase: Int,
)