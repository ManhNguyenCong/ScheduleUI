package com.example.scheduleui.data.localdatabase

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TypeDataConvert {

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

    @TypeConverter
    fun convertLocalDateTimeToString(ldt: LocalDateTime?): String? {
        return ldt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun fromStringToLocalDateTime(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
    }
}