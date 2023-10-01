package com.example.bookshelfapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    fun timestampToDate(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000 // Convert to milliseconds

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
        val year = calendar.get(Calendar.YEAR)

        val dayWithSuffix = getDayWithSuffix(day)

        return "$dayWithSuffix $month $year"
    }

    private fun getDayWithSuffix(day: Int): String {
        return when (day) {
            1, 21, 31 -> "${day}st"
            2, 22 -> "${day}nd"
            3, 23 -> "${day}rd"
            else -> "${day}th"
        }
    }
}
