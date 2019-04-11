package com.svyd.tasks.presentation.features.details

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

internal class TaskDetailsPresenterProvider : PresenterProvider() {

    fun providePresenter(args: Bundle): TaskDetailsContract.Presenter {
        return TaskDetailsPresenter(extractTask(args)!!, provideInteractor(), provideExceptionDelegate())
    }

    private fun provideInteractor(): PostInteractor<Task, Task> {
        return DeleteInteractor(provideRepository())
    }

    private fun extractTask(args: Bundle?): Task? {
        return if (args != null && args.containsKey(TaskDetailsActivity.EXTRA_TASK))
            args.getParcelable(TaskDetailsActivity.EXTRA_TASK)
        else
            throw IllegalArgumentException("No task model provided to start " + TaskDetailsActivity::class.java.simpleName)
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
