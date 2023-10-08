package com.example.scheduleui.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class DaySchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val day: Calendar,
)


