package com.svyd.tasks.presentation

import android.app.Application
import android.content.Context

import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.svyd.tasks.data.networking.framework.ServiceProvider
import com.svyd.tasks.data.networking.token.TokenManager
import com.svyd.tasks.data.networking.token.PreferenceTokenManager
import com.svyd.tasks.data.repository.reminders.FlowRemindersDAO
import com.svyd.tasks.data.repository.tasks.datasource.FlowTasksDAO
import com.svyd.tasks.data.repository.reminders.DefaultReminderRepository
import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.tasks.TasksService
import com.svyd.tasks.data.repository.tasks.datasource.CloudTasksDataSource
import com.svyd.tasks.data.repository.tasks.datasource.PersistentTasksDataSource
import com.svyd.tasks.presentation.base.sorting.SharedSortingPreference
import com.svyd.tasks.presentation.base.sorting.SortingPreference
import com.svyd.tasks.presentation.network.AndroidNetworkChangeDetector
import com.svyd.tasks.presentation.network.AndroidNetworkChangeHandler
import com.svyd.tasks.presentation.network.NetworkChangeDetector
import com.svyd.tasks.presentation.network.NetworkChangeHandler
import com.svyd.tasks.presentation.features.notification.AlarmNotificationScheduler
import com.svyd.tasks.presentation.features.notification.NotificationScheduler

class TasksApplication : Application() {

    lateinit var tokenManager: TokenManager
    lateinit var notificationScheduler: NotificationScheduler
    lateinit var networkChangeHandler: NetworkChangeHandler
    lateinit var networkChangeDetector: NetworkChangeDetector
    lateinit var sortingPreference: SortingPreference

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeTokenManager()
        initializeDatabase()
        initializeNotificationScheduler()
        initializeNetworkChangeDetector()
        initializeNetworkHandler()
        initializeSortingPreference()
    }

    private fun initializeSortingPreference() {
        sortingPreference = SharedSortingPreference(getSharedPreferences(SORTING_PREFERENCE_NAME, Context.MODE_PRIVATE))
    }

    private fun initializeNetworkChangeDetector() {
        networkChangeDetector = AndroidNetworkChangeDetector(this)
    }

    private fun initializeNetworkHandler() {
        networkChangeHandler = AndroidNetworkChangeHandler(provideTaskPersistenceDataSource(), provideTaskCloudDataSource())
    }

    private fun provideTaskPersistenceDataSource(): PersistentTasksDataSource {
        return PersistentTasksDataSource(FlowTasksDAO())
    }

    private fun provideTaskCloudDataSource(): CloudTasksDataSource {
        return CloudTasksDataSource(provideService(), FlowTasksDAO(), provideRemindersRepository())
    }

    private fun provideRemindersRepository(): RemindersRepository {
        return DefaultReminderRepository(FlowRemindersDAO(), notificationScheduler)
    }

    private fun provideService(): TasksService {
        return ServiceProvider().provideService(TasksService::class.java)
    }

    private fun initializeDatabase() = FlowManager.init(FlowConfig.Builder(this).build())

    private fun initializeNotificationScheduler() {
        notificationScheduler = AlarmNotificationScheduler(this)
    }

    private fun initializeTokenManager() {
        tokenManager = PreferenceTokenManager(getSharedPreferences(TOKEN_PREFERENCE_NAME, Context.MODE_PRIVATE))
    }

    companion object {
        lateinit var instance: TasksApplication
            private set

        private const val TOKEN_PREFERENCE_NAME = "token_preferences"
        private const val SORTING_PREFERENCE_NAME = "sorting_preferences"
    }
}
