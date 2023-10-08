package com.example.scheduleui.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    foreignKeys = [
        ForeignKey(
            DaySchedule::class,
            ["id"],
            ["dayScheduleId"],
            ForeignKey.CASCADE,
            ForeignKey.CASCADE
        )
    ]
)
data class JobSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val time: Calendar,
    @ColumnInfo
    val location: String,
    @ColumnInfo
    val notes: String,
    @ColumnInfo(name = "dayScheduleId", index = true)
    val dayScheduleId: Int
)
