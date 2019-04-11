package com.svyd.tasks.data.repository.tasks.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Task(var id: String,
           var title: String,
           var dueBy: Long,
           var reminderTime: Long,
           var priority: String,
           var operation: Int) : Parcelable {

    constructor() : this("0", "", 0, 0, "", TaskOperation.NO_OPERATION)
    constructor(id: String) : this(id, "", 0, 0, "", TaskOperation.NO_OPERATION)
    constructor(id: String, title: String, dueBy: Long, reminderTime: Long, priority: String) :
            this(id, title, dueBy, reminderTime, priority, TaskOperation.NO_OPERATION)

    fun getDate(): Long {
        return dueBy * 1000
    }

    fun setDate(date: Long) {
        this.dueBy = date / 1000
    }
}
