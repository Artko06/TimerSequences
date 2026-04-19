package com.example.timer.data.local.roomDB.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.timer.data.local.roomDB.entity.PhaseEntity
import com.example.timer.data.local.roomDB.entity.SequenceEntity

data class SequenceWithPhases(
    @Embedded
    val sequence: SequenceEntity,

    @Relation(
        entity = PhaseEntity::class,
        parentColumn = "sequence_id",
        entityColumn = "sequence_id"
    )
    val phases: List<PhaseEntity>
)
