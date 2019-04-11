package com.svyd.tasks.data.repository.tasks.datasource.factory

import com.svyd.tasks.data.networking.framework.ServiceProvider
import com.svyd.tasks.data.persistence.tasks.TasksDAO
import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.tasks.TasksService
import com.svyd.tasks.data.repository.tasks.datasource.CloudTasksDataSource
import com.svyd.tasks.data.repository.tasks.datasource.PersistentTasksDataSource
import com.svyd.tasks.data.repository.tasks.datasource.TasksDataSource
import com.svyd.tasks.presentation.network.NetworkChangeDetector

class DefaultDataSourceFactory(private val serversDAO: TasksDAO,
                               private val remindersRepository: RemindersRepository,
                               private val networkChangeDetector: NetworkChangeDetector)
    : TasksDataSourceFactory {
    override fun getPersistentDataSource(): PersistentTasksDataSource {
        return PersistentTasksDataSource (serversDAO)
    }

    override fun provideDataSource(): TasksDataSource {
        return if (networkChangeDetector.isOnline()) {
            CloudTasksDataSource(provideService(), serversDAO, remindersRepository)
        } else {
            PersistentTasksDataSource (serversDAO)
        }
    }

    private fun provideService() =
            ServiceProvider().provideService(TasksService::class.java)
}
