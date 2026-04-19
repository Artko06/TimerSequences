package com.example.timer.data.mappers

import com.example.timer.data.local.roomDB.entity.SequenceEntity
import com.example.timer.data.local.roomDB.entity.relation.SequenceWithPhases
import com.example.timer.domain.model.SequenceItem

fun SequenceWithPhases.toDomain(): SequenceItem {
    return SequenceItem(
        id = this.sequence.id,
        seqName = this.sequence.seqName,
        color = this.sequence.color,
        phases = this.phases.toDomain(),
        repeatCount = this.sequence.repeatCount,
    )
}

fun SequenceItem.toEntity(): SequenceEntity {
    return SequenceEntity(
        id = this.id,
        seqName = this.seqName,
        color = this.color,
        repeatCount = this.repeatCount,
    )
}