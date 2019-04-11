package com.svyd.tasks.presentation.features.update

import android.os.Bundle

import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.BaseObserver
import com.svyd.tasks.presentation.base.BasePresenter
import com.svyd.tasks.presentation.exception.BaseExceptionDelegate

import java.util.Calendar

abstract class BaseUpdateTaskPresenter internal constructor(delegate: BaseExceptionDelegate<*>) :
        BasePresenter<UpdateTaskContract.View>(delegate), UpdateTaskContract.Presenter {

    protected open lateinit var task: Task

    val currentTime: Long
        get() = Calendar.getInstance().time.time

    override fun initialize(view: UpdateTaskContract.View) {
        super.initialize(view)
        task = initTask()
    }

    protected abstract fun initTask(): Task

    override fun onTitleChanged(title: String) {
        task.title = title
    }

    override fun onPriorityChanged(priority: String) {
        task.priority = priority
    }

    override fun onDateChanged(timestamp: Long) {
        if (dateInFuture(timestamp)) {
            task.setDate(timestamp)
            view?.setDate(task.getDate())
        } else {
            view?.showDateInPastError()
        }
    }

    override fun onReminderTimeChanged(timeInMillis: Long) {
        task.reminderTime = timeInMillis
        view?.setReminder(timeInMillis.toInt())
    }

    protected fun dateInFuture(timestamp: Long): Boolean {
        //in scope of current minute
        return currentTime / (1000 * 60) <= timestamp / (1000 * 60)
    }

    override fun onDateClick() {
        if (task.getDate() != 0L) {
            view?.showDatePickerWithDate(task.getDate())
        } else {
            view?.showDatePickerWithCurrentDate()
        }
    }

    override fun onSaveClick() {
        if (validateText(task.title)) {
            executeSubscription()
        } else {
            view?.showEmptyTextError()
        }
    }

    override fun saveState(state: Bundle) {
        state.putParcelable(BasePresenter.KEY_INSTANCE_STATE, this.task)
    }

    override fun restoreState(state: Bundle) {
        val savedTask: Task? = state.getParcelable(BasePresenter.KEY_INSTANCE_STATE)
        if (savedTask != null) task = savedTask
    }

    abstract fun executeSubscription()

    private fun validateText(text: String?): Boolean {
        return text != null && text.isNotEmpty()
    }

    inner class CreateTaskObserver(presenter: BasePresenter<*>) : BaseObserver<Task>(presenter) {

        override fun onNext(t: Task) {
            handleResult(t)
        }
    }

    abstract fun handleResult(task: Task)
}
