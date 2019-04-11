package com.svyd.tasks.data.repository.tasks.datasource

import com.svyd.tasks.data.persistence.tasks.TasksDAO
import com.svyd.tasks.data.repository.reminders.RemindersRepository
import com.svyd.tasks.data.repository.tasks.TasksService
import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.data.repository.tasks.model.TaskRequest

import rx.Observable

class CloudTasksDataSource(private val service: TasksService,
                           private val database: TasksDAO,
                           private val remindersRepository: RemindersRepository) : TasksDataSource {

    override fun tasks(sortRequest: SortRequest): Observable<List<Task>> {
        return service.fetchServers(getQuery(sortRequest))
                .map { it.tasks }
                .flatMap { tasks -> database.storeTasks(tasks) }
    }

    private fun getQuery(sortRequest: SortRequest): String {
        return sortRequest.property + " " + getOrder(sortRequest.ascending)
    }

    private fun getOrder(ascending: Boolean): String {
        return if (ascending) {
            "asc"
        } else {
            "desc"
        }
    }

    override fun createTask(task: Task): Observable<Task> {
        return service.createTask(TaskRequest(task))
                .map { it.task }
                .flatMap { t -> database.createTask(t) }
    }

    override fun deleteTask(task: Task): Observable<Task> {
        return service.deleteTask(task.id)
                .flatMap { database.deleteTask(task) }
    }

    override fun updateTask(task: Task): Observable<Task> {
        return service.updateTask(task.id, TaskRequest(task))
                .flatMap { database.updateTask(task) }
    }

    fun deleteTasks(tasks: List<Task>): Observable<Task> {
        return Observable.from(tasks)
                .flatMap { task ->
                    service.deleteTask(task.id)
                            .map { task }
                }.flatMap { task -> database.deleteTask(task) }
    }

    fun createTasks(tasks: List<Task>): Observable<Task> {
        return Observable.from(tasks)
                .flatMap { localTask ->
                    service.createTask(TaskRequest(localTask))
                            .flatMap { remoteTask -> remindersRepository.remapReminder(localTask, remoteTask.task) }
                            .flatMap { database.deleteTask(localTask) }
                }
    }

    fun updateTasks(tasks: List<Task>): Observable<Task> {
        return Observable.from(tasks)
                .flatMap { task ->
                    service.updateTask(task.id, TaskRequest(task))
                            .map { task }
                }.flatMap { task -> database.deleteTask(task) }
    }
}
