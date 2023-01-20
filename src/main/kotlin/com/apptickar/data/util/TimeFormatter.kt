package com.apptickar.data.util

import java.util.concurrent.TimeUnit



    fun timeFormatter(time: Long) : String
    {
        var timeInMillis = time
        val days = TimeUnit.MILLISECONDS.toDays(timeInMillis)
        timeInMillis -= TimeUnit.DAYS.toMillis(days)
        val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
        timeInMillis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis)
        timeInMillis -=TimeUnit.MINUTES.toMillis(minutes)

        return if (days > 0) "$days days ago" else if (hours > 0)  "$hours hours ago" else "${TimeUnit.HOURS.toMinutes(hours) + minutes} minutes ago"
    }
