package com.svyd.tasks.data.persistence.tasks

import com.svyd.tasks.data.repository.tasks.model.Task
import rx.Observable

/**
 * Data Access Object for Task entities.
 */

interface TasksDAO {

    val tasks: Observable<List<Task>>
    val createdTasks: Observable<List<Task>>
    val updatedTasks: Observable<List<Task>>
    val deletedTasks: Observable<List<Task>>
    fun createTask(task: Task): Observable<Task>
    fun deleteTask(task: Task): Observable<Task>
    fun updateTask(task: Task): Observable<Task>
    fun storeTasks(tasks: List<Task>?): Observable<List<Task>>
    fun removeDeletedTasks(): Observable<Boolean>
    fun removeUpdatedTasks(): Observable<Boolean>
    fun removeCreatedTasks(): Observable<Boolean>
    fun clear()
}
