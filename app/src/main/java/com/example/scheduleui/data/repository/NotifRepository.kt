package com.example.scheduleui.data.repository

import com.example.scheduleui.data.localdatabase.ScheduleDao
import com.example.scheduleui.data.model.Notification
import kotlinx.coroutines.flow.Flow

class NotifRepository(
    private val scheduleDao: ScheduleDao
) {
    fun getAll(): Flow<List<Notification>> = scheduleDao.getAllNotification()

    fun getById(id: Int): Flow<Notification> = scheduleDao.getNotificationById(id)

    suspend fun insert(notif: Notification) = scheduleDao.insert(notif)

    suspend fun update(notif: Notification) = scheduleDao.update(notif)

    suspend fun delete(notif: Notification) = scheduleDao.delete(notif)
}