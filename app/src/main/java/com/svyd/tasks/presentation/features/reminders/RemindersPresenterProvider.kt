package com.svyd.tasks.presentation.features.reminders

import com.svyd.tasks.data.repository.reminders.FlowRemindersDAO
import com.svyd.tasks.data.persistence.reminders.RemindersDAO
import com.svyd.tasks.data.repository.reminders.DefaultReminderRepository
import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.presentation.TasksApplication
import com.svyd.tasks.presentation.base.Interactor
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.base.PresenterProvider
import com.svyd.tasks.presentation.features.notification.NotificationScheduler

class RemindersPresenterProvider : PresenterProvider() {

    fun providePresenter(): RemindersContract.Presenter {
        return RemindersPresenterImpl(provideExceptionDelegate(), provideRemindersInteractor(), provideDeleteReminderInteractor())
    }

    private fun provideDeleteReminderInteractor(): PostInteractor<Reminder, Reminder> {
        return DeleteReminderInteractor(provideRemindersRepository())
    }

    private fun provideRemindersRepository(): RemindersRepository {
        return DefaultReminderRepository(provideRemindersDao(), provideNotificationScheduler())
    }

    private fun provideNotificationScheduler(): NotificationScheduler {
        return TasksApplication.instance.notificationScheduler
    }

    private fun provideRemindersDao(): RemindersDAO {
        return FlowRemindersDAO()
    }

    private fun provideRemindersInteractor(): Interactor<MutableList<Reminder>> {
        return RemindersInteractor(provideRemindersRepository())
    }

}
