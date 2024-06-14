package com.example.scheduleui.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @ColumnInfo(name = "start")
    val timeStart: LocalTime,
    @ColumnInfo(name = "end")
    val timeEnd: LocalTime,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "teacher")
    val teacher: String,
    @ColumnInfo(name = "notes")
    val notes: String,
)
