package com.example.scheduleui.data.repository

import android.util.Log
import com.example.scheduleui.data.localdatabase.ScheduleDao
import com.example.scheduleui.data.model.DaySchedule
import com.example.scheduleui.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubjectRepository(
    private val scheduleDao: ScheduleDao
) {

    fun getSubjectsInDays(days: List<DaySchedule>): Flow<List<DaySchedule>> {
        return scheduleDao.getSubjectsInDays(days.map { it.date })
            .map { map ->
                Log.d(
                    TAG,
                    "getSubjectsInDays: " + map.toList()
                        .joinToString("\n") { it.first.toString() + " - " + it.second.toString() })

                days.map { day ->
                    if (day.date in map.keys) {
                        day.copy(subjects = map[day.date])
                    } else {
                        day
                    }
                }
            }
    }

}