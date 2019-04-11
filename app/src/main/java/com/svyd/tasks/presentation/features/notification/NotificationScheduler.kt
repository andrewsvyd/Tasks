package com.svyd.tasks.presentation.features.notification

import com.svyd.tasks.data.repository.reminders.model.Reminder

interface NotificationScheduler {
    fun scheduleNotification(reminder: Reminder)
    fun cancelNotification(reminder: Reminder)

    companion object {
        const val EXTRA_REMINDER_TITLE = "reminder_title"
        const val EXTRA_REMINDER_ID = "reminder_id"
        const val EXTRA_REMINDER_TIME = "reminder_time"
        const val REMINDER_CHANNEL = "reminders"
    }
}