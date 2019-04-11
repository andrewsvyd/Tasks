package com.svyd.tasks.data.repository.reminders

import com.svyd.tasks.data.persistence.reminders.RemindersDAO
import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.features.notification.NotificationScheduler
import rx.Completable

import rx.Observable

class DefaultReminderRepository(private val remindersDAO: RemindersDAO,
                                private val notificationScheduler: NotificationScheduler) : RemindersRepository {

    override fun clear(): Observable<Unit> {
        return remindersDAO.reminders
                .map { reminders -> cancelReminders(reminders) }
                .map { remindersDAO.clearAll() }
    }

    private fun cancelReminders(reminders: List<Reminder>) {
        reminders.forEach{reminder -> cancelNotification(reminder)}
    }

    override fun remapReminder(localTask: Task, remoteTask: Task): Observable<Task> {
        return Observable.just(getReminder(localTask))
                .map { reminder -> reminder?.let { remoteTask.reminderTime = reminder.reminderTime } }
                .flatMap { createReminder(remoteTask) }
                .flatMap { deleteReminder(localTask) }
    }

    override val reminders: Observable<MutableList<Reminder>>
        get() = remindersDAO.reminders

    override fun createReminder(task: Task): Observable<Task> {
        return if (hasReminder(task)) {
            remindersDAO.createReminder(task)
                    .map { scheduleNotification(task) }
        } else {
            Observable.just(task)
        }
    }

    private fun hasReminder(task: Task): Boolean {
        return task.reminderTime != 0L
    }

    override fun deleteReminder(task: Task): Observable<Task> {
        return remindersDAO.deleteReminder(task)
                .map { cancelNotification(task) }
    }

    override fun deleteReminder(reminder: Reminder): Observable<Reminder> {
        return remindersDAO.deleteReminder(reminder)
                .map { cancelNotification(reminder) }
    }

    override fun updateReminder(task: Task): Observable<Task> {
        return remindersDAO.updateReminder(task)
                .map { scheduleNotification(task) }
    }

    override fun getReminder(task: Task): Reminder? {
        return remindersDAO.getReminder(task)
    }

    private fun cancelNotification(task: Task): Task {
        cancelNotification(Reminder(task))
        return task
    }

    private fun cancelNotification(reminder: Reminder): Reminder {
        notificationScheduler.cancelNotification(reminder)
        return reminder
    }

    private fun scheduleNotification(task: Task): Task {
        if (task.reminderTime != 0L) {
            notificationScheduler.scheduleNotification(Reminder(task))
        }
        return task
    }

}
