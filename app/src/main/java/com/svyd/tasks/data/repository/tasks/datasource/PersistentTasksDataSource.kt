package com.svyd.tasks.data.repository.tasks.datasource

import com.svyd.tasks.data.persistence.tasks.TasksDAO
import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.data.repository.tasks.model.TaskOperation
import com.svyd.tasks.util.SortUtil

import rx.Observable

class PersistentTasksDataSource(private val database: TasksDAO) : TasksDataSource {

    override fun tasks(sortRequest: SortRequest): Observable<List<Task>> {
        return database.tasks
                .map { tasks -> tasks.sortedWith(SortUtil.getComparator(sortRequest)) }
    }

    val createdTasks: Observable<List<Task>>
        get() = database.createdTasks

    val updatedTasks: Observable<List<Task>>
        get() = database.updatedTasks

    val deletedTasks: Observable<List<Task>>
        get() = database.deletedTasks

    fun clearAll() {
        return database.clear()
    }

    override fun createTask(task: Task): Observable<Task> {
        task.operation = TaskOperation.CREATE
        return database.createTask(task)
    }

    override fun deleteTask(task: Task): Observable<Task> {
        return if (isRemote(task)) {
            task.operation = TaskOperation.DELETE
            database.updateTask(task)
        } else {
            database.deleteTask(task)
        }
    }

    override fun updateTask(task: Task): Observable<Task> {
        if (isRemote(task)) {
            task.operation = TaskOperation.UPDATE
        }
        return database.updateTask(task)
    }

    private fun isRemote(task: Task): Boolean {
        return task.operation != TaskOperation.CREATE
    }
}
