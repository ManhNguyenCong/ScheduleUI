package com.example.scheduleui.data.repository

import com.example.scheduleui.data.localdatabase.ScheduleDao
import com.example.scheduleui.data.model.DaySchedule
import com.example.scheduleui.data.model.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubjectRepository(
    private val scheduleDao: ScheduleDao
) {

    fun getSubjectsInDays(days: List<DaySchedule>): Flow<List<DaySchedule>> {

        return scheduleDao.getSubjectsInDays(days.map { it.date })
            .map { subjects ->
                days.map { day ->
                    val subjectInDay = subjects.filter { it.date.isEqual(day.date) }
                    if (!subjectInDay.isNullOrEmpty()) {
                        day.copy(subjects = subjectInDay)
                    } else {
                        day
                    }
                }
            }
    }

    fun getSubjectById(subjectId: Int): Flow<Subject> = scheduleDao.getSubjectById(subjectId)
    suspend fun insert(subject: Subject) = scheduleDao.insert(subject)

    suspend fun update(subject: Subject) = scheduleDao.update(subject)
    suspend fun delete(subject: Subject) = scheduleDao.delete(subject)

}