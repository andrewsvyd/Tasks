package com.svyd.tasks.data.persistence.reminders

import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.data.repository.tasks.model.Task
import rx.Observable

interface RemindersDAO {
    val reminders: Observable<MutableList<Reminder>>

    fun clearAll()
    fun getReminder(task: Task): Reminder?
    fun createReminder(task: Task): Observable<Task>
    fun deleteReminder(task: Task): Observable<Task>
    fun deleteReminder(reminder: Reminder): Observable<Reminder>
    fun updateReminder(task: Task): Observable<Task>
}
