package com.example.scheduleui.data

data class Subject(
    val id: Int,
    val name: String,
    val timeStart: String,
    val timeEnd: String,
    val location: String,
    val teacher: String,
    val notes: String,
    val loop: String,
    val dayScheduleId: Int
)
