package com.svyd.tasks.presentation.features.settings

import com.svyd.tasks.data.repository.tasks.TasksRepository
import com.svyd.tasks.presentation.base.Interactor
import rx.Observable

class LogOutInteractor(private val repository: TasksRepository): Interactor<Unit>() {

    override fun buildObserver(): Observable<out Unit> {
        return repository.clearAll()
    }
}
