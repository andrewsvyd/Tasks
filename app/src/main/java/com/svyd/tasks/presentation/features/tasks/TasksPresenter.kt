package com.svyd.tasks.presentation.features.tasks


import android.os.Bundle
import com.svyd.tasks.data.repository.tasks.model.SortRequest

import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.data.repository.tasks.model.Tasks
import com.svyd.tasks.presentation.base.BaseObserver
import com.svyd.tasks.presentation.base.BasePresenter
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.base.sorting.SortingPreference
import com.svyd.tasks.presentation.exception.BaseExceptionDelegate
import com.svyd.tasks.util.SortUtil.getComparator

import java.util.ArrayList

class TasksPresenter internal constructor(private val tasksInteractor: PostInteractor<SortRequest, List<Task>>,
                                          private val sortingPreference: SortingPreference,
                                          delegate: BaseExceptionDelegate<*>) :
        BasePresenter<TasksContract.View>(delegate), TasksContract.Presenter {

    private var tasks: Tasks = Tasks(ArrayList())

    override fun initialize(view: TasksContract.View) {
        super.initialize(view)
        fetchData()
    }

    private fun handleData(tasks: List<Task>) {
        this.tasks.tasks.clear()
        this.tasks.tasks.addAll(tasks)
        view?.showTasks(tasks)
    }

    override fun onTaskClick(id: String) {
        val task = findTask(id)
        task?.let { view?.navigateToTaskDetails(task) }
    }

    override fun onTaskDeleted(id: String) {
        tasks.tasks.remove(findTask(id))
        view?.updateList(tasks.tasks)
    }

    override fun onTaskUpdated(task: Task) {
        val currentTask = findTask(task.id)
        val index = tasks.tasks.indexOf(currentTask)
        tasks.tasks[index] = task
        sortTasks()
        view?.updateList(tasks.tasks)
    }

    private fun fetchData() {
        view?.showProgress()
        tasksInteractor.execute(sortingPreference.sorting, ServersObserver(this))
    }

    override fun onDataSynced() {
        fetchData()
        view?.showRefreshMessage()
    }

    override fun onRefresh() {
        fetchData()
    }

    override fun onTaskCreated(task: Task) {
        tasks.tasks.add(task)
        sortTasks()
        view?.updateList(tasks.tasks)
    }

    override fun onRemindersDeleted(deletedReminders: List<String>) {
        for (id in deletedReminders) {
            val task = findTask(id)
            if (task != null) {
                task.reminderTime = 0
            }
        }
    }

    override fun onSortChanged() {
        sortTasks()
        view?.updateList(tasks.tasks)
    }

    private fun sortTasks() {
        val sortRequest = sortingPreference.sorting
        tasks.tasks.sortWith(getComparator(sortRequest))
    }

    private fun findTask(id: String): Task? {
        for (task in tasks.tasks) {
            if (task.id == id) {
                return task
            }
        }
        return null
    }

    override fun restoreState(state: Bundle) {
        val savedTasks: Tasks? = state.getParcelable(BasePresenter.KEY_INSTANCE_STATE)
        if (savedTasks != null) tasks = savedTasks
    }

    override fun saveState(state: Bundle) {
        state.putParcelable(BasePresenter.KEY_INSTANCE_STATE, tasks)
    }

    override fun onStop() {
        tasksInteractor.unsubscribe()
    }

    internal inner class ServersObserver(presenter: BasePresenter<*>) : BaseObserver<List<Task>>(presenter) {

        override fun onCompleted() {
            view?.hideProgress()
        }

        override fun onNext(t: List<Task>) {
            handleData(t)
        }

    }
}
