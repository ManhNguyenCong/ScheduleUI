package com.example.scheduleui.data.localdatabase

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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

    @TypeConverter
    fun convertLocalDateToString(ld: LocalDate?): String? {
        return ld?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun fromStringToLocalDate(value: String?): LocalDate? {
        return value?.let {
            LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
        }
    }

    @TypeConverter
    fun convertLocalTimeToString(lt: LocalTime?): String? {
        return lt?.format(DateTimeFormatter.ISO_LOCAL_TIME)
    }

    @TypeConverter
    fun fromStringToLocalTime(value: String?): LocalTime? {
        return value?.let {
            LocalTime.parse(it, DateTimeFormatter.ISO_LOCAL_TIME)
        }
    }
}