package com.svyd.tasks.data.repository.reminders

import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.data.repository.tasks.model.Task
import rx.Observable

interface RemindersRepository {
    val reminders: Observable<MutableList<Reminder>>
    fun clear(): Observable<Unit>
    fun getReminder(task: Task): Reminder?
    fun createReminder(task: Task): Observable<Task>
    fun deleteReminder(task: Task): Observable<Task>
    fun deleteReminder(reminder: Reminder): Observable<Reminder>
    fun updateReminder(task: Task): Observable<Task>
    fun remapReminder(localTask: Task, remoteTask: Task): Observable<Task>
}
