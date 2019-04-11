package com.svyd.tasks.util

import com.svyd.tasks.R

object ReminderUtil {

    const val REMINDER_DISABLED = 0
    const val REMINDER_5_MINUTES = 1
    const val REMINDER_10_MINUTES = 2
    const val REMINDER_30_MINUTES = 3
    const val REMINDER_1_HOUR = 4

    private const val ONE_MINUTE_MILLISECONDS = 1000 * 60

    fun getTimeForReminder(reminder: Int): Long {
        return when (reminder) {
            REMINDER_DISABLED -> 0
            REMINDER_5_MINUTES -> (ONE_MINUTE_MILLISECONDS * 5).toLong()
            REMINDER_10_MINUTES -> (ONE_MINUTE_MILLISECONDS * 10).toLong()
            REMINDER_30_MINUTES -> (ONE_MINUTE_MILLISECONDS * 30).toLong()
            REMINDER_1_HOUR -> (ONE_MINUTE_MILLISECONDS * 60).toLong()
            else -> 0
        }
    }

    fun getReminderTextForTime(time: Int): Int {
        return when (time) {
            0 -> R.string.reminder_option_disabled
            ONE_MINUTE_MILLISECONDS * 5 -> R.string.reminder_option_5min
            ONE_MINUTE_MILLISECONDS * 10 -> R.string.reminder_option_10min
            ONE_MINUTE_MILLISECONDS * 30 -> R.string.reminder_option_30min
            ONE_MINUTE_MILLISECONDS * 60 -> R.string.reminder_option_hour
            else -> R.string.reminder_option_disabled
        }
    }

    fun getNotificationTextForTime(time: Int): Int {
        return when (time) {
            ONE_MINUTE_MILLISECONDS * 5 -> R.string.reminder_notification_5min
            ONE_MINUTE_MILLISECONDS * 10 -> R.string.reminder_notification_10min
            ONE_MINUTE_MILLISECONDS * 30 -> R.string.reminder_notification_30min
            ONE_MINUTE_MILLISECONDS * 60 -> R.string.reminder_notification_hour
            else -> R.string.text_reminder
        }
    }
}
