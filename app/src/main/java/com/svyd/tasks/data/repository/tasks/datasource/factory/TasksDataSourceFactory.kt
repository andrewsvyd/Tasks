package com.svyd.tasks.data.repository.tasks.datasource.factory

import com.svyd.tasks.data.repository.tasks.datasource.PersistentTasksDataSource
import com.svyd.tasks.data.repository.tasks.datasource.TasksDataSource

interface TasksDataSourceFactory {
    fun provideDataSource(): TasksDataSource
    fun getPersistentDataSource(): PersistentTasksDataSource
}
