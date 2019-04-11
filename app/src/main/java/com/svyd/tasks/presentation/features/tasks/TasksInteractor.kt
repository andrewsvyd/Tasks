package com.svyd.tasks.presentation.features.tasks

import com.svyd.tasks.data.repository.tasks.TasksRepository
import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.PostInteractor

import rx.Observable

class TasksInteractor(private val repository: TasksRepository) : PostInteractor<SortRequest, List<Task>>() {

    override fun buildPostObservable(data: SortRequest): Observable<List<Task>> {
        return repository.tasks(data)
    }

}
