package com.example.scheduleui.ui.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scheduleui.data.ScheduleDao

class NotificationViewModel(private val scheduleDao: ScheduleDao) : ViewModel() {

}

class NotificationViewModelFactory(private val scheduleDao: ScheduleDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(scheduleDao) as T
        }

        return super.create(modelClass)
    }
}