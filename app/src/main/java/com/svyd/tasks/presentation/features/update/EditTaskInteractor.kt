package com.svyd.tasks.presentation.features.update

import com.svyd.tasks.data.repository.tasks.TasksRepository
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.PostInteractor

import rx.Observable

class EditTaskInteractor internal constructor(private val tasksRepository: TasksRepository) : PostInteractor<Task, Task>() {

    override fun buildPostObservable(data: Task): Observable<Task> {
        return tasksRepository.updateTask(data)
    }
}
