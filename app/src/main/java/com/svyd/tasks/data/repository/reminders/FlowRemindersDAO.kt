package com.svyd.tasks.data.repository.reminders

import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import com.svyd.tasks.data.persistence.reminders.FlowReminderModel
import com.svyd.tasks.data.persistence.reminders.FlowReminderModel_Table
import com.svyd.tasks.data.persistence.reminders.RemindersDAO
import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.data.repository.tasks.model.Task

import rx.Observable

class FlowRemindersDAO : RemindersDAO {

    override fun clearAll() {
        Delete.table(FlowReminderModel::class.java)
    }

    override val reminders: Observable<MutableList<Reminder>>
        get() = Observable.create { subscriber ->
            try {
                subscriber.onNext(map(Select()
                        .from(FlowReminderModel::class.java)
                        .queryList()))
                subscriber.onCompleted()
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }

    override fun createReminder(task: Task): Observable<Task> {
        return Observable.create { subscriber ->
            try {
                FlowReminderModel(Reminder(task)).save()
                subscriber.onNext(task)
                subscriber.onCompleted()
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    override fun deleteReminder(task: Task): Observable<Task> {
        FlowReminderModel(Reminder(task)).delete()
        return Observable.create { subscriber ->
            try {
                FlowReminderModel(Reminder(task)).delete()
                subscriber.onNext(task)
                subscriber.onCompleted()
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    override fun updateReminder(task: Task): Observable<Task> {
        return Observable.create { subscriber ->
            try {
                if (task.reminderTime != 0L) {
                    if (!FlowReminderModel(Reminder(task)).update()) {
                        FlowReminderModel(Reminder(task)).save()
                    }
                } else {
                    FlowReminderModel(Reminder(task)).delete()
                }
                subscriber.onNext(task)
                subscriber.onCompleted()
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    override fun getReminder(task: Task): Reminder? {
        return map(Select()
                .from(FlowReminderModel::class.java)
                .where(FlowReminderModel_Table.taskId.`is`(task.id))
                .querySingle())
    }

    private fun map(reminders: List<FlowReminderModel>): MutableList<Reminder> {
        val flowReminders: ArrayList<Reminder> = ArrayList()
        reminders.forEach { flowReminder ->
            map(flowReminder)?.let { reminder ->
                flowReminders.add(reminder)
            }
        }
        return flowReminders
    }

    private fun map(reminder: FlowReminderModel?): Reminder? {
        return reminder?.let { Reminder(reminder.taskId, reminder.reminderTime, reminder.taskName, reminder.taskTime) }
    }

    override fun deleteReminder(reminder: Reminder): Observable<Reminder> {
        FlowReminderModel(reminder).delete()
        return Observable.just(reminder)
    }
}
