package com.svyd.tasks.presentation.features.details

import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.BaseContract


interface TaskDetailsContract {

    interface View : BaseContract.View {
        fun setText(title: String)
        fun setPriority(priority: String)
        fun setDate(date: Long)
        fun showDeletePrompt()
        fun navigateBackWithDeletion(id: String)
        fun navigateToEditTask(task: Task)
        fun setReminder(time: Int)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onDeleteClick()
        fun onDeleteConfirm()
        fun onEditClick()
        fun onTaskEdited(task: Task)
    }
}
