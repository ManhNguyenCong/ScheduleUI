package com.example.scheduleui.data

import android.app.Application

class ScheduleApplication : Application() {
    val database: ScheduleDatabase by lazy {
        ScheduleDatabase.getDatabase(this)
    }
}