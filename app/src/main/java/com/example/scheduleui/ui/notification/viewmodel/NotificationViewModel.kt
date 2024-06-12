package com.example.scheduleui.ui.notification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduleui.data.model.Notification
import com.example.scheduleui.data.localdatabase.ScheduleDao
import com.example.scheduleui.util.getDateFromString
import com.example.scheduleui.util.getTimeFromTimeString
import kotlinx.coroutines.launch
import java.util.Calendar

class NotificationViewModel(private val scheduleDao: ScheduleDao) : ViewModel() {

    // Get all notification in local database
    val notifications = scheduleDao.getAllNotification().asLiveData()

    /**
     * This function is used to get notification by id
     *
     * @param id
     */
    fun getNotificationById(id: Int): LiveData<Notification> {
        return scheduleDao.getNotificationById(id).asLiveData()
    }

    // INSERT
    /**
     * This function is used to insert a notification
     *
     * @param notification
     */
    private fun insert(notification: Notification) {
        viewModelScope.launch {
            scheduleDao.insertNotification(notification)
        }
    }

    // UPDATE
    /**
     * This function is used to update a notification
     *
     * @param notification
     */
    private fun update(notification: Notification) {
        viewModelScope.launch {
            scheduleDao.updateNotification(notification)
        }
    }

    // DELETE
    /**
     * This function is used to delete a notification
     *
     * @param notification
     */
    fun delete(notification: Notification) {
        viewModelScope.launch {
            scheduleDao.deleteNotification(notification)
        }
    }

    /**
     * This function is used to validate notification entry
     *
     * @param name
     */
    fun validNotificationEntry(name: String): Boolean {
        return name.isNotEmpty()
    }

    /**
     * This function is used to add new a notification
     *
     * @param name
     * @param content
     * @param day
     * @param time
     */
    fun addNewNotification(
        name: String,
        content: String,
        day: String,
        time: String,
        loopOption: String
    ) {
        // Get date choose
        val dateChoose = day.getDateFromString()
        // Get time (hourOfDay, minute) choose
        val timeChoose = time.getTimeFromTimeString()
        // Get time (date, hourOfDay, minute)
        val getTime = Calendar.Builder()
            .setDate(
                dateChoose.get(Calendar.YEAR),
                dateChoose.get(Calendar.MONTH),
                dateChoose.get(Calendar.DAY_OF_MONTH)
            )
            .setTimeOfDay(timeChoose.get(Calendar.HOUR_OF_DAY), timeChoose.get(Calendar.MINUTE), 0)
            .build()

        // Insert notification
        if(loopOption == "Không lặp lại") {
            insert(Notification(0, name, content, getTime, false))
        } else {
            insert(Notification(0, name, content, getTime, true))
        }
    }

    /**
     * This function is used to update a notification
     *
     * @param id
     * @param name
     * @param content
     * @param day
     * @param time
     * @param loopOption
     */
    fun updateNotification(
        id: Int,
        name: String,
        content: String,
        day: String,
        time: String,
        loopOption: String
    ) {
        // Get date choose
        val dateChoose = day.getDateFromString()
        // Get time (hourOfDay, minute) choose
        val timeChoose = time.getTimeFromTimeString()
        // Get time (date, hourOfDay, minute)
        val getTime = Calendar.Builder()
            .setDate(
                dateChoose.get(Calendar.YEAR),
                dateChoose.get(Calendar.MONTH),
                dateChoose.get(Calendar.DAY_OF_MONTH)
            )
            .setTimeOfDay(timeChoose.get(Calendar.HOUR_OF_DAY), timeChoose.get(Calendar.MINUTE), 0)
            .build()

        // Insert notification
        if(loopOption == "Không lặp lại") {
            update(Notification(id, name, content, getTime, false))
        } else {
            update(Notification(id, name, content, getTime, true))
        }
    }
}

class NotificationViewModelFactory(private val scheduleDao: ScheduleDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(scheduleDao) as T
        }

        return super.create(modelClass)
    }
}