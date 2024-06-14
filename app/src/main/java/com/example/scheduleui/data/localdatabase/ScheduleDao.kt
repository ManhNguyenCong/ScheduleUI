package com.example.scheduleui.data.localdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scheduleui.data.model.Notification
import com.example.scheduleui.data.model.Subject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ScheduleDao {

    // Subject
    @Query("SELECT * FROM Subject")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM Subject WHERE id = :id")
    fun getSubjectById(id: Int): Flow<Subject>

    @MapInfo(keyColumn = "date")
    @Query("SELECT date, * FROM Subject WHERE date IN (:days)")
    fun getSubjectsInDays(
        days: List<LocalDate>
    ): Flow<List<Subject>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(subject: Subject)

    @Update
    suspend fun update(subject: Subject)

    @Delete
    suspend fun delete(subject: Subject)

    //Notification
    @Query("SELECT * FROM Notification ORDER BY name")
    fun getAllNotification(): Flow<List<Notification>>

    @Query("SELECT * FROM Notification WHERE id = :notificationId")
    fun getNotificationById(notificationId: Int): Flow<Notification>

    @Insert
    suspend fun insert(notification: Notification)

    @Update
    suspend fun update(notification: Notification)

    @Delete
    suspend fun delete(notification: Notification)
}