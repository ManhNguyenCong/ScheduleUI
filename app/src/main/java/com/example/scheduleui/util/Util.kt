package com.example.scheduleui.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import java.util.Calendar

/**
 * This function is used to format time to pattern "hh:mm"
 */
fun Calendar.formatDayScheduleTime(): String {
    return String.format("%02d:%02d", this.get(Calendar.HOUR_OF_DAY), this.get(Calendar.MINUTE))
}

/**
 * This function is used to format date to pattern "dayOfWeek, dd/mm/y"
 */
fun Calendar.formatDayScheduleDate(): String {
    val dayOfWeek = when (this.get(Calendar.DAY_OF_WEEK)) {
        1 -> "Chủ nhật"
        2 -> "Thứ hai"
        3 -> "Thứ ba"
        4 -> "Thứ tư"
        5 -> "Thứ năm"
        6 -> "Thứ sáu"
        else -> "Thứ bảy"
    }

    return String.format(
        "%s, %02d/%02d/%d",
        dayOfWeek,
        this.get(Calendar.DAY_OF_MONTH),
        this.get(Calendar.MONTH) + 1,
        this.get(Calendar.YEAR)
    )
}

/**
 * This function is used to set time to 00:00:00
 */
fun Calendar.setDefaultTime(): Calendar {
    return Calendar.Builder()
        .setDate(
            this.get(Calendar.YEAR),
            this.get(Calendar.MONTH),
            this.get(Calendar.DAY_OF_MONTH)
        ).build()
}

/**
 * This function is used to get time from a string template "hour:minute"
 */
fun String.getTimeFromTimeString(): Calendar {
    val timeValues = this.split(":").map { str -> str.toInt() }
    return Calendar.Builder().setTimeOfDay(timeValues[0], timeValues[1], 0).build()
}

/**
 * This function is used to get date from a string template "dayOfWeek, day/month/year"
 */
fun String.getDateFromString(): Calendar {
    val dateValues = this.substring(this.indexOf(", ") + 1).trim().split("/").map { it.toInt() }
    return Calendar.Builder().setDate(dateValues[2], dateValues[1] - 1, dateValues[0]).build()
}

/**
 * This function is used to findNavController safer :)))
 *
 */
fun Fragment.findNavControllerSafely(): NavController? {
    return try {
        findNavController()
    } catch (e: IllegalStateException) {
        null
    }
}