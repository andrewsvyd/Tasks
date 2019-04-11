package com.svyd.tasks.data.repository.reminders.model

import android.os.Parcelable
import com.svyd.tasks.data.repository.tasks.model.Task
import kotlinx.android.parcel.Parcelize

@Parcelize
class Reminder(val taskId: String,
               val reminderTime: Long,
               val taskName: String,
               val taskTime: Long ): Parcelable {
    constructor(task: Task) : this(task.id, task.reminderTime, task.title, task.dueBy)
}
