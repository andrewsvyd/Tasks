package com.svyd.tasks.presentation.features.reminders

import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.presentation.base.BaseContract
import java.util.ArrayList

interface RemindersContract {
    interface View : BaseContract.View {
        fun showReminders(reminders: List<Reminder>)
        fun undoDeletion(deletedPosition: Int)
        fun setDeletedReminders(removedReminders: ArrayList<String>)
        fun showEmptyList()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun onReminderDelete(position: Int)
    }
}