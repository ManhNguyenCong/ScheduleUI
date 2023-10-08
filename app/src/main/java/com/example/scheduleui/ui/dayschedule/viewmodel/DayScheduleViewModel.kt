package com.example.scheduleui.ui.dayschedule.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduleui.data.DaySchedule
import com.example.scheduleui.data.ScheduleDao
import com.example.scheduleui.data.Subject
import com.example.scheduleui.util.formatDayScheduleDate
import com.example.scheduleui.util.getDateFromString
import com.example.scheduleui.util.getTimeFromTimeString
import com.example.scheduleui.util.setDefaultTime
import kotlinx.coroutines.launch
import java.util.Calendar

class DayScheduleViewModel(private val scheduleDao: ScheduleDao) : ViewModel() {

    // Get all day schedule from local database
    val daySchedules: LiveData<List<DaySchedule>> = scheduleDao.getAllDaySchedule().asLiveData()

    /**
     * This function is used to get list subject by dayScheduleId
     *
     * @param dayScheduleId
     */
    fun getSubjectByDayScheduleId(dayScheduleId: Int): LiveData<List<Subject>> {
        return scheduleDao.getSubjectByDayScheduleId(dayScheduleId).asLiveData()
    }

    /**
     * This function is used to get subject by subject id
     *
     * @param subjectId
     */
    fun getSubjectById(subjectId: Int): LiveData<Subject> {
        return scheduleDao.getSubjectById(subjectId).asLiveData()
    }


    // INSERT
    /**
     * This function is used to insert a day schedule
     *
     * @param daySchedule
     */
    private fun insertDaySchedule(daySchedule: DaySchedule) {
        viewModelScope.launch {
            scheduleDao.insertDaySchedule(daySchedule)
        }
    }

    /**
     * This function is used to insert a subject
     *
     * @param subject
     */
    private fun insertSubject(subject: Subject) {
        viewModelScope.launch {
            scheduleDao.insertSubject(subject)
        }
    }

    // UPDATE
    /**
     * This function is used to update a subject
     *
     * @param subject
     */
    private fun updateSubject(subject: Subject) {
        viewModelScope.launch {
            scheduleDao.updateSubject(subject)
        }
    }

    // DELETE
    /**
     * This function is used to delete a subject
     *
     *
     */
    fun deleteSubject(subject: Subject) {
        viewModelScope.launch {
            scheduleDao.deleteSubject(subject)
        }
    }

    /**
     * This function is used to add day schedule for first time use app
     */
    fun addDaySchedulesForFirstTime() {
        viewModelScope.launch {
            scheduleDao.getAllDaySchedule().collect {
                if (it.isEmpty()) {
                    insertDaySchedule(
                        DaySchedule(
                            0,
                            Calendar.Builder()
                                .setInstant(Calendar.getInstance().timeInMillis - 14 * DAY_TO_MILLIS)
                                .build().setDefaultTime()
                        )
                    )
                } else if (it.size < 30) {
                    insertDaySchedule(
                        DaySchedule(
                            0,
                            Calendar.Builder()
                                .setInstant(it.last().day.timeInMillis + DAY_TO_MILLIS)
                                .build()
                        )
                    )
                }
            }
        }
    }

    /**
     * This function is used to add DaySchedule if recycle view is scroll to head or tail
     *
     * @param dayTarget
     */
    fun addDayScheduleToDay(dayTarget: Calendar) {
        viewModelScope.launch {
            scheduleDao.getAllDaySchedule().collect {
                if (it.size < 30) return@collect

                if (it.first().day > dayTarget) {
                    insertDaySchedule(
                        DaySchedule(
                            0,
                            Calendar.Builder()
                                .setInstant(it.first().day.timeInMillis - DAY_TO_MILLIS).build()
                        )
                    )
                } else if (it.last().day < dayTarget) {
                    insertDaySchedule(
                        DaySchedule(
                            0,
                            Calendar.Builder()
                                .setInstant(it.last().day.timeInMillis + DAY_TO_MILLIS).build()
                        )
                    )
                }
            }
        }
    }

    /**
     * This function is used to check "is auto add day to DaySchedule list completed?"
     *
     * @param targetDay
     *
     * @return true if daySchedules has targetDay
     */
    fun checkAutoAddDayCompleted(daySchedules: List<DaySchedule>, targetDay: Calendar): Boolean {
        return daySchedules.find { daySchedule -> daySchedule.day == targetDay } != null
    }

    /**
     * This function is used to check validation of time entry,
     * return true if timeStart <= timeEnd else false
     *
     * @param timeStart
     * @param timeEnd
     */
    fun validTimeEntry(timeStart: Calendar, timeEnd: Calendar): Boolean {
        return timeStart <= timeEnd
    }

    /**
     * This function is used to check validation of date entry,
     * return true if dayStart < dayEnd else false
     *
     * @param dayStart
     * @param dayEnd
     */
    fun validDateEntry(dayStart: Calendar, dayEnd: Calendar): Boolean {
        viewModelScope.launch {
            scheduleDao.getAllDaySchedule().collect { listDaySchedule ->
                if (dayStart > dayEnd) {
                    // if dayStart larger, check dayStart, if don't in daySchedules,
                    // add day to dayStart
                    listDaySchedule.find { daySchedule ->
                        daySchedule.day == dayStart
                    } ?: addDayScheduleToDay(dayStart)
                } else {
                    // Check dayEnd, if don't in daySchedules,
                    // add day to dayEnd
                    listDaySchedule.find { daySchedule ->
                        daySchedule.day == dayEnd
                    } ?: addDayScheduleToDay(dayStart)
                }
            }
        }
        Log.d("TKB", "validDateEntry")

        return dayStart < dayEnd
    }

    /**
     * This function is used to valid subject entry
     *
     * @param subjectName
     */
    fun isEntryValid(subjectName: String): Boolean {
        return subjectName.isNotEmpty()
    }

    /**
     * This function is used to add new a subject
     *
     * @param name
     * @param timeStart
     * @param timeEnd
     * @param location
     * @param teacher
     * @param notes
     * @param loop
     * @param date
     */
    fun addNewASubject(
        name: String,
        timeStart: String,
        timeEnd: String,
        location: String,
        teacher: String,
        notes: String,
        loop: String,
        date: String
    ) {
        viewModelScope.launch {
            scheduleDao.getAllDaySchedule().collect {
                val dayScheduleId =
                    it.find { daySchedule -> daySchedule.day == date.getDateFromString() }?.id ?: -1

                if (dayScheduleId == -1) {
                    return@collect
                } else {
                    insertSubject(
                        Subject(
                            0,
                            name,
                            timeStart.getTimeFromTimeString(),
                            timeEnd.getTimeFromTimeString(),
                            location,
                            teacher,
                            notes,
                            loop,
                            dayScheduleId
                        )
                    )
                }
            }
        }
    }

    /**
     * This function is used to add new a list of subject with the same loop content
     *
     */
    fun addNewSubjects(
        name: String,
        timeStart: String,
        timeEnd: String,
        location: String,
        teacher: String,
        notes: String,
        loop: String,
        dayStart: String,
        dayEnd: String
    ) {
        // Get dayOfWeeks loop
        val dayOfWeeks = getDayOfWeeksFromLoopContent(loop)

        viewModelScope.launch {
            scheduleDao.getAllDaySchedule().collect { listDaySchedule ->
                // Get index of day start
                val startIndex =
                    listDaySchedule.indexOf(listDaySchedule.find { daySchedule ->
                        daySchedule.day == dayStart.getDateFromString()
                    })
                // Get index of day end
                val endIndex =
                    listDaySchedule.indexOf(listDaySchedule.find { daySchedule ->
                        daySchedule.day == dayEnd.getDateFromString()
                    })

                for (index in startIndex..endIndex) {
                    // Get day in dayStart..dayEnd
                    val day = listDaySchedule[index].day
                    // Check if dayOfWeek in dayOfWeeks, add new a subject in this day
                    if (day.get(Calendar.DAY_OF_WEEK) - 1 in dayOfWeeks) {
                        // Add new a subject
                        addNewASubject(
                            name,
                            timeStart,
                            timeEnd,
                            location,
                            teacher,
                            notes,
                            loop,
                            day.formatDayScheduleDate()
                        )
                    }
                }
            }
        }

    }

    /**
     * This function is used to get day of weeks with the same a subject
     */
    private fun getDayOfWeeksFromLoopContent(loopContent: String): List<Int> {
        // String "loop" has template : "dayOfWeeks-dayStart-dayEnd"
        val dayOfWeeks = loopContent.split("-")[0]

        // In dayOfWeeks, day of week is split by ", "
        return dayOfWeeks.split(", ").map {
            it.toInt()
        }
    }

    /**
     * This function is used to update a subject
     *
     * @param subjectId
     * @param name
     * @param timeStart
     * @param timeEnd
     * @param location
     * @param teacher
     * @param notes
     * @param dayScheduleId
     */
    fun updateSubject(
        subjectId: Int,
        name: String,
        timeStart: String,
        timeEnd: String,
        location: String,
        teacher: String,
        notes: String,
        loop: String,
        date: String
    ) {
        viewModelScope.launch {
            scheduleDao.getAllDaySchedule().collect {
                val dayScheduleId = it.find { daySchedule -> daySchedule.day == date.getDateFromString() }?.id ?: return@collect

                updateSubject(
                    Subject(
                        subjectId,
                        name,
                        timeStart.getTimeFromTimeString(),
                        timeEnd.getTimeFromTimeString(),
                        location,
                        teacher,
                        notes,
                        loop,
                        dayScheduleId
                    )
                )
            }
        }
    }


    companion object {
        // Convert 1 day to millisecond
        const val DAY_TO_MILLIS = 24 * 3_600_000
    }
}

class DayScheduleViewModelFactory(private val scheduleDao: ScheduleDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DayScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DayScheduleViewModel(scheduleDao) as T
        }
        return super.create(modelClass)
    }
}