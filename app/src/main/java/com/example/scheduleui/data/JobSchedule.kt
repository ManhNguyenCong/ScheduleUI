package com.example.scheduleui.data

data class JobSchedule(
    val id: Int,
    val name: String,
    val time: String,
    val location: String,
    val notes: String,
    val dayScheduleId: Int
)
