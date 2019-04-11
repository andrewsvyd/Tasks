package com.svyd.tasks.presentation.features.settings

import com.svyd.tasks.data.persistence.reminders.RemindersDAO
import com.svyd.tasks.data.persistence.tasks.TasksDAO
import com.svyd.tasks.data.repository.reminders.DefaultReminderRepository
import com.svyd.tasks.data.repository.reminders.FlowRemindersDAO
import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.tasks.DefaultTasksRepository
import com.svyd.tasks.data.repository.tasks.TasksRepository
import com.svyd.tasks.data.repository.tasks.datasource.FlowTasksDAO
import com.svyd.tasks.data.repository.tasks.datasource.factory.DefaultDataSourceFactory
import com.svyd.tasks.data.repository.tasks.datasource.factory.TasksDataSourceFactory
import com.svyd.tasks.presentation.TasksApplication
import com.svyd.tasks.presentation.base.Interactor
import com.svyd.tasks.presentation.base.PresenterProvider
import com.svyd.tasks.presentation.base.sorting.SortingPreference
import com.svyd.tasks.presentation.features.notification.NotificationScheduler
import com.svyd.tasks.presentation.network.NetworkChangeDetector

internal class SettingsPresenterProvider: PresenterProvider() {
    fun providePresenter(): SettingsContract.Presenter {
        return SettingsPresenter(provideSortingPreference(), provideLogOutInteractor(), provideExceptionDelegate())
    }

    private fun provideLogOutInteractor(): Interactor<Unit> {
        return LogOutInteractor(provideRepository())
    }

    private fun provideRepository(): TasksRepository {
        return DefaultTasksRepository(provideDataSourceFactory(), provideReminderRepository())
    }

    private fun provideReminderRepository(): RemindersRepository {
        return DefaultReminderRepository(provideRemindersDAO(), provideNotificationScheduler())
    }

    private fun provideNotificationScheduler(): NotificationScheduler {
        return TasksApplication.instance.notificationScheduler
    }

    private fun provideRemindersDAO(): RemindersDAO {
        return FlowRemindersDAO()
    }

    private fun provideDataSourceFactory(): TasksDataSourceFactory {
        return DefaultDataSourceFactory(provideDatabase(), provideReminderRepository(), provideNetworkDetector())
    }

    private fun provideNetworkDetector(): NetworkChangeDetector {
        return TasksApplication.instance.networkChangeDetector
    }

    private fun provideDatabase(): TasksDAO {
        return FlowTasksDAO()
    }

    private fun provideSortingPreference(): SortingPreference {
        return TasksApplication.instance.sortingPreference
    }
}