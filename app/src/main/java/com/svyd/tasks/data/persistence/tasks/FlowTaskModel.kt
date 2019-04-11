package com.svyd.tasks.data.persistence.tasks

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.svyd.tasks.data.repository.tasks.model.TaskOperation
import com.svyd.tasks.data.repository.tasks.model.Task

@Table(database = DBFlowTasksDatabase::class, name = "tasks")
internal class FlowTaskModel : BaseModel {

    @PrimaryKey
    @Column
    lateinit var id: String

    @Column
    lateinit var title: String

    @Column
    var date: Long = 0

    @Column
    lateinit var priority: String

    @Column
    var timestamp: Long = 0

    @Column
    var operation: Int = TaskOperation.NO_OPERATION

    constructor(): super()

    constructor(task: Task) {
        title = task.title
        date = task.dueBy
        priority = task.priority
        operation = task.operation
        timestamp = System.currentTimeMillis()

        id = if (task.id.toLong() == 0L ) {
            timestamp.toString()
        } else {
            task.id
        }
    }
}
