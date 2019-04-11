package com.svyd.tasks.data.repository.tasks

import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.tasks.datasource.factory.TasksDataSourceFactory
import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.Task

import rx.Observable
import java.lang.Exception

class DefaultTasksRepository(private val dataSourceFactory: TasksDataSourceFactory,
                             private val remindersRepository: RemindersRepository) : TasksRepository {

    override fun clearAll(): Observable<Unit> {
        return remindersRepository.clear()
                .map { dataSourceFactory.getPersistentDataSource().clearAll() }
    }

    override fun tasks(sortRequest: SortRequest): Observable<List<Task>> {
        return dataSourceFactory.provideDataSource().tasks(sortRequest)
                .map { t -> addReminders(t) }
    }

    private fun addReminders(tasks: List<Task>): List<Task> {
        tasks.forEach { task -> run { remindersRepository.getReminder(task)?.let { reminder -> task.reminderTime = reminder.reminderTime } } }
        return tasks
    }

    override fun createTask(task: Task): Observable<Task> =
            dataSourceFactory.provideDataSource().createTask(task)
                    .flatMap { remoteTask -> remindersRepository.createReminder(remoteTask.also { t -> t.reminderTime = task.reminderTime }) }

    override fun deleteTask(task: Task): Observable<Task> =
            dataSourceFactory.provideDataSource().deleteTask(task)
                    .flatMap { remoteTask -> remindersRepository.deleteReminder(remoteTask.also { t -> t.reminderTime = task.reminderTime }) }

    override fun updateTask(task: Task): Observable<Task> =
            dataSourceFactory
                    .provideDataSource().updateTask(task)
                    .flatMap { remoteTask -> remindersRepository.updateReminder(remoteTask.also { t -> t.reminderTime = task.reminderTime }) }
}
