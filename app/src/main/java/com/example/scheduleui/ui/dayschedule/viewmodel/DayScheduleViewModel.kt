package com.example.scheduleui.ui.dayschedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.scheduleui.data.model.DaySchedule
import com.example.scheduleui.data.repository.SubjectRepository
import java.time.LocalDate

class DayScheduleViewModel(private val subjectRepo: SubjectRepository) : ViewModel() {

    fun getSubjectsInRecentDays(): LiveData<List<DaySchedule>> {
        val days = mutableListOf<DaySchedule>()
        val today = LocalDate.now()
        for (i in 0..6) {
            days.add(DaySchedule(today.plusDays(i * 1L), null))
        }

        return subjectRepo.getSubjectsInDays(days).asLiveData()
    }

    companion object {
        // Convert 1 day to millisecond
        const val DAY_TO_MILLIS = 24 * 3_600_000
    }
}

class DayScheduleViewModelFactory(private val subjectRepo: SubjectRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DayScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DayScheduleViewModel(subjectRepo) as T
        }
        return super.create(modelClass)
    }
}