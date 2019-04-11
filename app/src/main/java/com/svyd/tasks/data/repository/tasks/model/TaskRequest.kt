package com.svyd.tasks.data.repository.tasks.model

class TaskRequest(task: Task) {
    val id: Int = task.id.toLong().toInt()
    val title: String = task.title
    val dueBy: Long = task.dueBy
    val priority: String = task.priority
}
