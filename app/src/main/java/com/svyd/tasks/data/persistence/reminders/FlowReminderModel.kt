package com.svyd.tasks.data.persistence.reminders

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.svyd.tasks.data.persistence.tasks.DBFlowTasksDatabase
import com.svyd.tasks.data.repository.reminders.model.Reminder

@Table(database = DBFlowTasksDatabase::class, name = "reminders")
internal class FlowReminderModel: BaseModel {

    constructor(): super()

    constructor(reminder: Reminder) {
        taskName = reminder.taskName
        taskId = reminder.taskId
        reminderTime = reminder.reminderTime
        taskTime = reminder.taskTime
    }

    @PrimaryKey
    @Column
    lateinit var taskId: String

    @Column
    var reminderTime: Long = 0

    @Column
    var taskTime: Long = 0

    @Column
    lateinit var taskName: String
}
