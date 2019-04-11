package com.svyd.tasks.presentation.features.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.svyd.tasks.R
import com.svyd.tasks.util.ReminderUtil

class NotificationReceiver  : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val reminderName = intent!!.getStringExtra(NotificationScheduler.EXTRA_REMINDER_TITLE)
        val reminderId = intent.getStringExtra(NotificationScheduler.EXTRA_REMINDER_ID)
        val reminderTime = intent.getLongExtra(NotificationScheduler.EXTRA_REMINDER_TIME, 0)
        val mBuilder = NotificationCompat.Builder(context!!, NotificationScheduler.REMINDER_CHANNEL)
                .setSmallIcon(R.drawable.vector_bell)
                .setContentTitle(context.getString(ReminderUtil.getNotificationTextForTime(reminderTime.toInt())))
                .setContentText(reminderName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NotificationScheduler.REMINDER_CHANNEL,
                    context.getString(R.string.notification_channel_reminders),
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(reminderId.toLong().toInt(), mBuilder.build())
    }
}
