package com.example.timer.data.local.roomDB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sequences"
)
data class SequenceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sequence_id")
    val id: Long = 0,

    @ColumnInfo(name = "seq_name")
    val seqName: String,

    @ColumnInfo(name = "color")
    val color: Int,

    @ColumnInfo(name = "repeat_count")
    val repeatCount: Int = 1,
)
