package com.svyd.tasks.presentation.features.update

import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.BaseContract

interface UpdateTaskContract {
    interface View: BaseContract.View {
        fun setCreateTitle()
        fun setEditTitle()
        fun showDatePickerWithCurrentDate()
        fun showDatePickerWithDate(date: Long)
        fun showDateInPastError()
        fun setDate(date: Long)
        fun setReminder(date: Int)
        fun showEmptyTextError()
        fun navigateBackWithEditedResult(task: Task)
        fun setPriority(priority: String)
        fun setText(title: String)
        fun navigateBackWithCreatedResult(task: Task)
        fun showReminderInPastError()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun onTitleChanged(title: String)
        fun onPriorityChanged(priority: String)
        fun onDateChanged(timestamp: Long)
        fun onDateClick()
        fun onSaveClick()
        fun onReminderTimeChanged(timeInMillis: Long)
    }
}