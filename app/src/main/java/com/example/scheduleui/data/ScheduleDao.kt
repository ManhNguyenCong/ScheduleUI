package com.example.scheduleui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    // DaySchedule
    @Query("SELECT * FROM DaySchedule ORDER BY day")
    fun getAllDaySchedule(): Flow<List<DaySchedule>>

    @Query("SELECT * FROM DaySchedule WHERE id = :id")
    fun getDayScheduleById(id: Int): Flow<DaySchedule>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDaySchedule(daySchedule: DaySchedule)

    @Update
    suspend fun updateDaySchedule(daySchedule: DaySchedule)

    @Delete
    suspend fun deleteDaySchedule(daySchedule: DaySchedule)

    // Subject
    @Query("SELECT * FROM Subject")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM Subject WHERE id = :id")
    fun getSubjectById(id: Int): Flow<Subject>

    @Query("SELECT * FROM Subject WHERE dayScheduleId = :dayScheduleId")
    fun getSubjectByDayScheduleId(dayScheduleId: Int): Flow<List<Subject>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubject(subject: Subject)

    @Update
    suspend fun updateSubject(subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    //Notification
    @Query("SELECT * FROM Notification ORDER BY name")
    fun getAllNotification(): Flow<List<Notification>>

    @Query("SELECT * FROM Notification WHERE id = :notificationId")
    fun getNotificationById(notificationId: Int): Flow<Notification>

    @Insert
    suspend fun insertNotification(notification: Notification)

    @Update
    suspend fun updateNotification(notification: Notification)

    @Delete
    suspend fun deleteNotification(notification: Notification)
}