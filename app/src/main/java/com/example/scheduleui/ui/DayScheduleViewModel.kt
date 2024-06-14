package com.example.scheduleui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduleui.data.localdatabase.SettingPreferences
import com.example.scheduleui.data.model.DaySchedule
import com.example.scheduleui.data.model.Notification
import com.example.scheduleui.data.model.Subject
import com.example.scheduleui.data.repository.NotifRepository
import com.example.scheduleui.data.repository.SubjectRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DayScheduleViewModel(
    private val subjectRepo: SubjectRepository,
    private val notifRepo: NotifRepository,
    private val settingPrefs: SettingPreferences
) : ViewModel() {

    fun getSubjectsInRecentDays(date: LocalDate): LiveData<List<DaySchedule>> {
        val days = mutableListOf<DaySchedule>()
        for (i in 0..6) {
            days.add(DaySchedule(date.plusDays(i * 1L), null))
        }

        return subjectRepo.getSubjectsInDays(days).asLiveData()
    }

    fun getSubjectById(subjectId: Int): LiveData<Subject> {
        return subjectRepo.getSubjectById(subjectId).asLiveData()
    }

    fun insert(
        name: String,
        timeStart: LocalTime,
        timeEnd: LocalTime,
        dateStart: LocalDate,
        dateEnd: LocalDate,
        teacher: String,
        location: String,
        notes: String,
        loop: BooleanArray
    ) {
        if (loop.contains(true)) {
            var date = LocalDate.of(dateStart.year, dateStart.monthValue, dateStart.dayOfMonth)
            while (!date.isAfter(dateEnd)) {
                if (loop[date.dayOfWeek.value % 7]) {
                    insert(
                        Subject(0, name, date, timeStart, timeEnd, location, teacher, notes)
                    )
                }
                date = date.plusDays(1)
            }
        } else {
            insert(Subject(0, name, dateStart, timeStart, timeEnd, location, teacher, notes))
        }
    }

    private fun insert(subject: Subject) {
        viewModelScope.launch {
            subjectRepo.insert(subject)
        }
    }

    fun update(subject: Subject) {
        viewModelScope.launch {
            subjectRepo.update(subject)
        }
    }

    fun delete(subject: Subject) {
        viewModelScope.launch {
            subjectRepo.delete(subject)
        }
    }

    fun getNotifications(): LiveData<List<Notification>> {
        return notifRepo.getAll().asLiveData()
    }

    fun getNotifById(notifId: Int): LiveData<Notification> {
        return notifRepo.getById(notifId).asLiveData()
    }

    fun create(title: String, content: String, datetime: LocalDateTime, isLoop: Boolean) {
        viewModelScope.launch {
            notifRepo.insert(Notification(0, title, content, datetime, isLoop))
        }
    }

    fun update(notif: Notification) {
        viewModelScope.launch {
            notifRepo.update(notif)
        }
    }

    fun delete(notif: Notification) {
        viewModelScope.launch {
            notifRepo.delete(notif)
        }
    }

    fun getNameSetting() = settingPrefs.nameSetting
    fun getSchoolSetting() = settingPrefs.schoolSetting
    fun getOpacitySetting() = settingPrefs.opacitySetting

    fun setNameSetting(name: String) {
        viewModelScope.launch {
            settingPrefs.setName(name)
        }
    }

    fun setSchoolSetting(school: String) {
        viewModelScope.launch {
            settingPrefs.setSchool(school)
        }
    }

    fun setOpacitySetting(opacity: Float) {
        viewModelScope.launch {
            settingPrefs.setOpacity(opacity)
        }
    }
}

class DayScheduleViewModelFactory(
    private val subjectRepo: SubjectRepository,
    private val notifRepo: NotifRepository,
    private val settingPrefs: SettingPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DayScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return DayScheduleViewModel(
                subjectRepo,
                notifRepo,
                settingPrefs
            ) as T
        }
        return super.create(modelClass)
    }
}