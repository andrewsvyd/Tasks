package com.svyd.tasks.presentation.features.update

import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.exception.BaseExceptionDelegate

class EditTaskPresenter internal constructor(private val updateTaskInteractor: PostInteractor<Task, Task>,
                                             override var task: Task,
                                             delegate: BaseExceptionDelegate<*>) : BaseUpdateTaskPresenter(delegate) {

    override fun initialize(view: UpdateTaskContract.View) {
        super.initialize(view)
        view.setEditTitle()
        view.setDate(task.getDate())
        view.setPriority(task.priority)
        view.setText(task.title)
        onReminderTimeChanged(task.reminderTime)
    }

    override fun initTask(): Task {
        return task
    }

    override fun executeSubscription() {
        updateTaskInteractor.execute(task, CreateTaskObserver(this))
    }

    override fun onStop() {
        updateTaskInteractor.unsubscribe()
    }

    override fun handleResult(task: Task) {
        view?.navigateBackWithEditedResult(task)
    }
}
