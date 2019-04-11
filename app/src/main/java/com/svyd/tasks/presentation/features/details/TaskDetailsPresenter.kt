package com.svyd.tasks.presentation.features.details

import android.os.Bundle

import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.BaseObserver
import com.svyd.tasks.presentation.base.BasePresenter
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.exception.BaseExceptionDelegate

class TaskDetailsPresenter internal constructor(private var task: Task,
                                                private val deleteInteractor: PostInteractor<Task, Task>,
                                                delegate: BaseExceptionDelegate<*>) :
        BasePresenter<TaskDetailsContract.View>(delegate),
        TaskDetailsContract.Presenter {

    override fun initialize(view: TaskDetailsContract.View) {
        super.initialize(view)
        refreshUi()
    }

    private fun refreshUi() {
        view?.setText(task.title)
        view?.setPriority(task.priority)
        view?.setDate(task.getDate())
        view?.setReminder(task.reminderTime.toInt())
    }

    override fun onDeleteClick() {
        view?.showDeletePrompt()
    }

    override fun onDeleteConfirm() {
        deleteInteractor.execute(task, DeleteObserver(this))
    }

    override fun onEditClick() {
        view?.navigateToEditTask(task)
    }

    override fun onTaskEdited(task: Task) {
        this.task = task
        refreshUi()
    }

    override fun restoreState(state: Bundle) {
        val savedTask: Task? = state.getParcelable(BasePresenter.KEY_INSTANCE_STATE)
        if (savedTask != null) task = savedTask
    }

    override fun saveState(state: Bundle) {
        state.putParcelable(BasePresenter.KEY_INSTANCE_STATE, task)
    }

    override fun onStop() {
        deleteInteractor.unsubscribe()
    }

    private inner class DeleteObserver internal constructor(presenter: BasePresenter<*>) : BaseObserver<Task>(presenter) {

        override fun onNext(t: Task) {
            view?.navigateBackWithDeletion(t.id)
        }
    }
}
