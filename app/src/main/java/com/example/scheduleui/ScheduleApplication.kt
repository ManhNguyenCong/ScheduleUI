package com.example.scheduleui

import android.app.Application
import com.example.scheduleui.data.localdatabase.ScheduleDatabase

class ScheduleApplication : Application() {
    val database: ScheduleDatabase by lazy {
        ScheduleDatabase.getDatabase(this)
    }
}