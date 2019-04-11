package com.svyd.tasks.presentation.features.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import com.svyd.tasks.data.repository.reminders.model.Reminder

import java.util.Calendar

import android.content.Context.ALARM_SERVICE
import com.svyd.tasks.presentation.features.notification.NotificationScheduler.Companion.EXTRA_REMINDER_ID
import com.svyd.tasks.presentation.features.notification.NotificationScheduler.Companion.EXTRA_REMINDER_TIME
import com.svyd.tasks.presentation.features.notification.NotificationScheduler.Companion.EXTRA_REMINDER_TITLE

class AlarmNotificationScheduler(private val context: Context) : NotificationScheduler {

    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    override fun scheduleNotification(reminder: Reminder) {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = reminder.taskTime * 1000 - reminder.reminderTime
        calendar.set(Calendar.SECOND, 0)

        if (calendar.before(Calendar.getInstance())) {
            return
        }

        // Enable a receiver

        val receiver = ComponentName(context, NotificationReceiver::class.java)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)

        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(EXTRA_REMINDER_TITLE, reminder.taskName)
        intent.putExtra(EXTRA_REMINDER_ID, reminder.taskId)
        intent.putExtra(EXTRA_REMINDER_TIME, reminder.reminderTime)

        val pendingIntent = PendingIntent.getBroadcast(context, reminder.taskId.toLong().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun cancelNotification(reminder: Reminder) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, reminder.taskId.toLong().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
