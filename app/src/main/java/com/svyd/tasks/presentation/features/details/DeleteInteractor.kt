package com.svyd.tasks.presentation.features.details

import com.svyd.tasks.data.repository.tasks.TasksRepository
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.PostInteractor

import rx.Observable

class DeleteInteractor internal constructor(private val repository: TasksRepository) : PostInteractor<Task, Task>() {

    override fun buildPostObservable(data: Task): Observable<Task> {
        return repository.deleteTask(data)
    }
}
