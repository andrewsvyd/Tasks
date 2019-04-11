package com.svyd.tasks.presentation.features.update

import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.exception.BaseExceptionDelegate

class CreateTaskPresenter internal constructor(private val createTaskInteractor: PostInteractor<Task, Task>,
                                               delegate: BaseExceptionDelegate<*>) :
        BaseUpdateTaskPresenter(delegate) {

    override fun initialize(view: UpdateTaskContract.View) {
        super.initialize(view)
        view.setCreateTitle()
        onDateChanged(currentTime)
        view.setReminder(0)
    }

    override fun onReminderTimeChanged(timeInMillis: Long) {
        if (validateReminder(timeInMillis)) {
            task.reminderTime = timeInMillis
            view?.setReminder(timeInMillis.toInt())
        } else {
            view?.showReminderInPastError()
        }
    }

    private fun validateReminder(reminderTimeBefore: Long): Boolean {
        return dateInFuture(task.getDate() - reminderTimeBefore)
    }

    override fun initTask(): Task {
        return Task()
    }

    override fun executeSubscription() {
        createTaskInteractor.execute(task, CreateTaskObserver(this))
    }

    override fun onStop() {
        createTaskInteractor.unsubscribe()
    }

    override fun handleResult(task: Task) {
        view?.navigateBackWithCreatedResult(task)
    }
}
