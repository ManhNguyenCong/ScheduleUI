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
    @Query("SELECT date, * FROM Subject WHERE date IN (:days) GROUP BY date")
    fun getSubjectsInDays(
        days: List<LocalDate>
    ): Flow<Map<LocalDate, List<Subject>>>

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