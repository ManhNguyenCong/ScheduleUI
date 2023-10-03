package com.example.scheduleui.data

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class ScheduleDatabaseTest : TestCase() {

    private lateinit var database: ScheduleDatabase
    private lateinit var dao: ScheduleDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ScheduleDatabase::class.java).build()
        dao = ScheduleDatabase.getDatabase(context).scheduleDao()
        dao = ScheduleApplication().database.scheduleDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertDayScheduleTest() = runBlocking {
        val daySchedule = DaySchedule(0, Calendar.getInstance())

        dao.insertDaySchedule(daySchedule)

        val result = dao.getAllDaySchedule().asLiveData().getOrAwaitValue()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(daySchedule.day.timeInMillis, result[0].day.timeInMillis)
    }

    @Test
    fun updateDayScheduleTest() = runBlocking {
        val date = Calendar.getInstance()
        val daySchedule = DaySchedule(0, date)

        dao.insertDaySchedule(daySchedule)
        var result = dao.getAllDaySchedule().asLiveData().getOrAwaitValue()

        val dayScheduleUpdated = DaySchedule(
            result[0].id,
            Calendar.Builder().setInstant(date.timeInMillis + 1000).build()
        )
        dao.updateDaySchedule(dayScheduleUpdated)

        result = dao.getAllDaySchedule().asLiveData().getOrAwaitValue()
        Assert.assertEquals(1, result.size)
        Assert.assertEquals(dayScheduleUpdated.day.timeInMillis, result[0].day.timeInMillis)
    }

    @Test
    fun deleteDayScheduleTest() = runBlocking {
        val daySchedule = DaySchedule(0, Calendar.getInstance())

        dao.insertDaySchedule(daySchedule)
        var result = dao.getAllDaySchedule().asLiveData().getOrAwaitValue()

        val dayScheduleDeleted = result[0]
        dao.deleteDaySchedule(dayScheduleDeleted)

        result = dao.getAllDaySchedule().asLiveData().getOrAwaitValue()

        Assert.assertEquals(0, result.size)
    }

    @Test
    fun insertSubject() = runBlocking {
        val date = Calendar.getInstance()

        val daySchedule = DaySchedule(0, date)
        dao.insertDaySchedule(daySchedule)
        val daySchedules = dao.getAllDaySchedule().asLiveData().getOrAwaitValue()

        val subject = Subject(
            0,
            "Subject 1",
            date,
            date,
            "Location A",
            "Teacher A",
            "Notes...",
            "Loop 1",
            daySchedules[0].id
        )
        dao.insertSubject(subject)
        val result = dao.getAllSubjects().asLiveData().getOrAwaitValue()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(subject.name, result[0].name)
    }

    @Test
    fun updateSubject() = runBlocking {
        val date = Calendar.getInstance()

        val daySchedule = DaySchedule(0, date)
        dao.insertDaySchedule(daySchedule)
        val daySchedules = dao.getAllDaySchedule().asLiveData().getOrAwaitValue()

        val subject = Subject(
            0,
            "Subject 1",
            date,
            date,
            "Location A",
            "Teacher A",
            "Notes...",
            "Loop 1",
            daySchedules[0].id
        )
        dao.insertSubject(subject)
        var result = dao.getAllSubjects().asLiveData().getOrAwaitValue()

        val subjectUpdated = Subject(
            result[0].id,
            "Subject name",
            date,
            date,
            "Location",
            "Teacher",
            "Notes",
            "Loop",
            result[0].dayScheduleId
        )
        dao.updateSubject(subjectUpdated)
        result = dao.getAllSubjects().asLiveData().getOrAwaitValue()
        Assert.assertEquals(1, result.size)
        Assert.assertEquals(subjectUpdated.name, result[0].name)
    }

    @Test
    fun deleteSubject() = runBlocking {
        val date = Calendar.getInstance()

        val daySchedule = DaySchedule(0, date)
        dao.insertDaySchedule(daySchedule)
        val daySchedules = dao.getAllDaySchedule().asLiveData().getOrAwaitValue()

        val subject = Subject(
            0,
            "Subject 1",
            date,
            date,
            "Location A",
            "Teacher A",
            "Notes...",
            "Loop 1",
            daySchedules[0].id
        )
        dao.insertSubject(subject)
        var result = dao.getAllSubjects().asLiveData().getOrAwaitValue()

        val subjectDeleted = result[0]
        dao.deleteSubject(subjectDeleted)
        result = dao.getAllSubjects().asLiveData().getOrAwaitValue()
        Assert.assertEquals(0, result.size)
    }
}