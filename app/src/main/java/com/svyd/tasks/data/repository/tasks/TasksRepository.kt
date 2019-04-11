package com.svyd.tasks.data.repository.tasks

import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.Task

import rx.Observable

interface TasksRepository {
    fun tasks(sortRequest: SortRequest): Observable<List<Task>>
    fun createTask(task: Task): Observable<Task>
    fun deleteTask(task: Task): Observable<Task>
    fun updateTask(task: Task): Observable<Task>
    fun clearAll(): Observable<Unit>
}
