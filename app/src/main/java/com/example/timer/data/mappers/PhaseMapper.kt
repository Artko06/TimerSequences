package com.example.timer.data.mappers

import com.example.timer.data.local.roomDB.entity.PhaseEntity
import com.example.timer.domain.model.PhaseItem
import com.example.timer.domain.model.PhaseType

fun List<PhaseEntity>.toDomain(): List<PhaseItem> {
    return this.map { phaseEntity ->
        PhaseItem(
            id = phaseEntity.id,
            type = PhaseType.valueOf(value = phaseEntity.type),
            durationPhase = phaseEntity.durationPhase,
        )
    }
}

fun toPhaseEntities(
    sequenceId: Long,
    phases: List<PhaseItem>)
: List<PhaseEntity> {
    return phases.mapIndexed { index, phase ->
        PhaseEntity(
            id = phase.id,
            sequenceId = sequenceId,
            type = phase.type.name,
            durationPhase = phase.durationPhase,
            numberPhase = index + 1
        )
    }
}