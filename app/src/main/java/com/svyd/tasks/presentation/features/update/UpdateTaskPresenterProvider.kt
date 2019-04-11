package com.svyd.tasks.presentation.features.update

import android.os.Bundle

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
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.TasksApplication
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.base.PresenterProvider
import com.svyd.tasks.presentation.network.NetworkChangeDetector
import com.svyd.tasks.presentation.features.notification.NotificationScheduler

internal class UpdateTaskPresenterProvider : PresenterProvider() {

    fun providePresenter(args: Bundle?): UpdateTaskContract.Presenter {
        val task = extractTask(args)
        return if (task != null) {
            EditTaskPresenter(provideEditInteractor(), task, provideExceptionDelegate())
        } else {
            CreateTaskPresenter(provideCreateInteractor(), provideExceptionDelegate())
        }
    }

    private fun provideEditInteractor(): PostInteractor<Task, Task> {
        return EditTaskInteractor(provideRepository())
    }

    private fun extractTask(args: Bundle?): Task? {
        return if (args != null && args.containsKey(UpdateTaskActivity.EXTRA_TASK))
            args.getParcelable(UpdateTaskActivity.EXTRA_TASK)
        else
            null
    }

    private fun provideCreateInteractor(): PostInteractor<Task, Task> {
        return CreateTaskInteractor(provideRepository())
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
