package com.example.scheduleui.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "time")
    val time: LocalDateTime,
    @ColumnInfo(name = "loop")
    val isLoop: Boolean
)