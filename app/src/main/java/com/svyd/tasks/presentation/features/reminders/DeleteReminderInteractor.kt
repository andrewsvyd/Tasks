package com.svyd.tasks.presentation.features.reminders

import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.presentation.base.PostInteractor
import rx.Observable

class DeleteReminderInteractor(private val repository: RemindersRepository): PostInteractor<Reminder, Reminder>() {
    override fun buildPostObservable(data: Reminder): Observable<Reminder> {
        return repository.deleteReminder(data)
    }
}
