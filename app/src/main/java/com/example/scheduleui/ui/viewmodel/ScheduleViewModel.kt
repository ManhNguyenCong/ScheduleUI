package com.example.scheduleui.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scheduleui.data.DaySchedule
import com.example.scheduleui.data.JobSchedule
import com.example.scheduleui.data.Notification
import com.example.scheduleui.data.Subject
import java.util.Calendar

class ScheduleViewModel() : ViewModel() {

    val daySchedules = mutableListOf<DaySchedule>(
        DaySchedule(0, "Thứ bảy, 16/09/2023"),
        DaySchedule(1, "Chủ nhật, 17/09/2023"),
        DaySchedule(2, "Thứ hai, 18/09/2023"),
        DaySchedule(3, "Thứ ba, 19/09/2023"),
        DaySchedule(4, "Thứ tư, 20/09/2023"),
        DaySchedule(5, "Thứ năm, 21/09/2023"),
        DaySchedule(6, "Thứ sáu, 22/09/2023")
    )

    val subjects = mutableListOf<Subject>(
        Subject(0, "Toán rời rạc", "7:00", "9:40", "901A9", "Teacher A", "", "", 0),
        Subject(1, "Nhập môn lập trình", "9:30", "12:00", "801A1", "Teacher B", "", "", 0),
        Subject(2, "Toán rời rạc", "7:00", "9:40", "901A1", "Teacher A", "", "", 3),
        Subject(3, "Bóng chuyền", "7:00", "8:40", "A6", "Teacher C", "", "", 2),
        Subject(4, "Nhập môn lập trình", "9:30", "12:00", "801A1", "Teacher B", "", "", 3),
        Subject(
            5,
            "Tiếng anh Công nghệ thông tin cơ bản 1",
            "7:00",
            "8:40",
            "901A1",
            "Teacher D",
            "",
            "",
            5
        ),
        Subject(
            6,
            "Tiếng anh Công nghệ thông tin cơ bản 1",
            "7:00",
            "8:40",
            "901A1",
            "Teacher D",
            "",
            "",
            6
        )
    )

    val notifications = mutableListOf<Notification>(
        Notification(0, "Đi học", "Mang laptop", "Sat, 09/16/2023, 7:00"),
        Notification(1, "Nắp mạng", "", "Mon, 11/16/2023, 7:00"),
        Notification(2, "Đi ngủ", "Ngủ đi, chơi ít thôi", "00:00 - Hằng ngày")
    )

    val jobs = mutableListOf<JobSchedule>(
        JobSchedule(0, "Job A", "00:00 - 05:00", "Location...", "Notes...", 3)
    )

    /**
     * This fun is used to get a subject by id
     *
     * @param subjectId
     */
    fun getSubjectById(subjectId: Int): Subject? {
        return subjects.find { subject -> subject.id == subjectId }
    }

    /**
     * This fun is used to get a day schedule by id
     *
     * @param dayScheduleId
     */
    fun getDayScheduleById(dayScheduleId: Int): DaySchedule? {
        return daySchedules.find { daySchedule -> daySchedule.id == dayScheduleId }
    }

    // Insert
    fun insertSubject(subject: Subject) {
        subjects.add(subject)
    }

    // Update

    // Delete
    /**
     * This function is used to delete a subject with subject id
     */
    fun deleteSubject(subjectId: Int) {
        var index = 0
        while (index < subjects.size && subjects[index].id != subjectId) {
            index++
        }
        subjects.removeAt(index)
    }

    /**
     * This function is used to get loop content as a template (dayOfWeeks-dayStart-dayEnd)
     *
     * @param dayOfWeeks
     * @param dayStart
     * @param dayEnd
     */
    private fun getLoopContent(
        dayOfWeeks: String,
        dayStart: String,
        dayEnd: String
    ): String {
        if (dayOfWeeks.isEmpty()) {
            return ""
        }

        return String.format(
            "%s-%s-%s",
            dayOfWeeks,
            dayStart,
            dayEnd
        )
    }

    /**
     * This function is used to check validation of time entry,
     * return true if timeStart <= timeEnd else false
     *
     * @param timeStart
     * @param timeEnd
     */
    fun validTimeEntry(timeStart: Calendar, timeEnd: Calendar): Boolean {
        return timeStart.timeInMillis <= timeEnd.timeInMillis
    }

    /**
     * This function is used to check validation of date entry,
     * return true if dayStart <= dayEnd else false
     *
     * @param dayStart
     * @param dayEnd
     */
    fun validDateEntry(dayStart: Calendar, dayEnd: Calendar): Boolean {
        return dayStart.timeInMillis <= dayEnd.timeInMillis
    }

    /**
     * This function is used to set time of day to 00:00:00
     *
     * @param date
     */
    fun setDefaultTimeForDate(date: Calendar): Calendar {
        return Calendar.Builder().setDate(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        ).build()
    }

    /**
     * This function is used to set date = 01/01/1970 and set second = 0
     *
     * @param time
     */
    fun setDefaultDateForTime(time: Calendar): Calendar {
        return Calendar.Builder().setTimeOfDay(
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            0
        ).build()
    }

    /**
     * This function is used to get time from a string, default date: 01/01/1970
     *
     * @param strTime
     */
    fun getTimeFromStringTime(strTime: String): Calendar {
        val temp = strTime.split(":").map { str -> str.toInt() }

        return Calendar.Builder().setTimeOfDay(temp[0], temp[1], 0).build()
    }

    /**
     * This function is used to format time to a string template (hour:minute)
     *
     * @param hourOfDay
     * @param minute
     */
    fun getStringTimeFromTime(hourOfDay: Int, minute: Int): String {
        return String.format("%02d:%02d", hourOfDay, minute)
    }

    /**
     * This function is used to get date from a string, default time: 00:00:00
     *
     * @param strDate
     */
    fun getDateFromStringDate(strDate: String): Calendar {
        val temp = strDate.split("/").map { str -> str.toInt() }

        return Calendar.Builder().setDate(temp[2], temp[1], temp[0]).build()
    }

    /**
     * This function is used to format date to a string template (date/month/year)
     *
     * @param dayOfMonth
     * @param month
     * @param year
     */
    fun getStringDateFromDate(dayOfMonth: Int, month: Int, year: Int): String {
        return String.format(
            "%02d/%02d/%d",
            dayOfMonth,
            month,
            year
        )
    }
}

class ScheduleViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel() as T
        }
        return super.create(modelClass)
    }
}