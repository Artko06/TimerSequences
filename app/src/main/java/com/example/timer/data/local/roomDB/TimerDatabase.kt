package com.example.timer.data.local.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.timer.data.local.roomDB.dao.PhasesDao
import com.example.timer.data.local.roomDB.dao.SequencesDao
import com.example.timer.data.local.roomDB.entity.PhaseEntity
import com.example.timer.data.local.roomDB.entity.SequenceEntity

@Database(
    entities = [SequenceEntity::class, PhaseEntity::class],
    version = 1
)
abstract class TimerDatabase: RoomDatabase() {
    abstract fun sequencesDao(): SequencesDao

    abstract fun phasesDao(): PhasesDao

    companion object {
        const val DATABASE_NAME = "TIMER_ROOM_DATABASE"
    }
}