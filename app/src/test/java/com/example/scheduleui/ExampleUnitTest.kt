package com.example.scheduleui

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val calendar = Calendar.getInstance()
        assertEquals(1, calendar.get(Calendar.DAY_OF_WEEK))
    }
}