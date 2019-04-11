package com.svyd.tasks.presentation.features.reminders

import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.presentation.base.Interactor
import rx.Observable

class RemindersInteractor(private val repository: RemindersRepository): Interactor<MutableList<Reminder>>() {
    override fun buildObserver(): Observable<MutableList<Reminder>> {
        return repository.reminders
    }
}
