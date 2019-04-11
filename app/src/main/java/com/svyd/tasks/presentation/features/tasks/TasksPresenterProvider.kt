package com.svyd.tasks.presentation.features.tasks

import com.svyd.tasks.data.repository.reminders.FlowRemindersDAO
import com.svyd.tasks.data.repository.tasks.datasource.FlowTasksDAO
import com.svyd.tasks.data.persistence.reminders.RemindersDAO
import com.svyd.tasks.data.persistence.tasks.TasksDAO
import com.svyd.tasks.data.repository.reminders.DefaultReminderRepository
import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.tasks.DefaultTasksRepository
import com.svyd.tasks.data.repository.tasks.TasksRepository
import com.svyd.tasks.data.repository.tasks.datasource.factory.DefaultDataSourceFactory
import com.svyd.tasks.data.repository.tasks.datasource.factory.TasksDataSourceFactory
import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.TasksApplication
import com.svyd.tasks.presentation.base.Interactor
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.base.PresenterProvider
import com.svyd.tasks.presentation.base.sorting.SortingPreference
import com.svyd.tasks.presentation.network.NetworkChangeDetector
import com.svyd.tasks.presentation.features.notification.NotificationScheduler

class TasksPresenterProvider : PresenterProvider() {

    fun providePresenter(): TasksContract.Presenter {
        return TasksPresenter(provideTasksInteractor(), provideSortPreference(), provideExceptionDelegate())
    }

    private fun provideSortPreference(): SortingPreference {
        return TasksApplication.instance.sortingPreference
    }

    private fun provideTasksInteractor(): PostInteractor<SortRequest, List<Task>> {
        return TasksInteractor(provideRepository())
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
}
