package com.svyd.tasks.presentation.features.tasks

import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.BaseContract

interface TasksContract {
    interface View: BaseContract.View {
        fun showTasks(tasks: List<Task>)
        fun navigateToTaskDetails(task: Task)
        fun updateList(tasks: List<Task>)
        fun showRefreshMessage()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun onTaskClick(id: String)
        fun onTaskDeleted(id: String)
        fun onTaskUpdated(task: Task)
        fun onTaskCreated(task: Task)
        fun onRemindersDeleted(deletedReminders: List<String>)
        fun onDataSynced()
        fun onRefresh()
        fun onSortChanged()
    }
}