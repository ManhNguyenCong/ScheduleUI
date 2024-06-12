package com.example.scheduleui.data.model

import java.time.LocalDate

data class DaySchedule(
    val date: LocalDate,
    val subjects: List<Subject>?
)


