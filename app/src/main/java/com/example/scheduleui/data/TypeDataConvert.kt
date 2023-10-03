package com.example.scheduleui.data

import androidx.room.TypeConverter
import java.util.Calendar

class TypeDataConvert {

    @TypeConverter
    fun fromCalenderToLong(cal: Calendar?): Long? {
        return cal?.timeInMillis
    }

    @TypeConverter
    fun fromLongToCalender(value: Long?): Calendar? {
        return value?.let {
            Calendar.Builder().setInstant(value).build()
        }
    }
}