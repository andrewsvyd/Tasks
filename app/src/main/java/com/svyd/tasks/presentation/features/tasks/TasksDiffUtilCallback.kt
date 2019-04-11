package com.svyd.tasks.presentation.features.tasks

import com.svyd.tasks.data.repository.tasks.model.Task

import androidx.recyclerview.widget.DiffUtil

class TasksDiffUtilCallback(private val oldTasks: List<Task>, private val newTasks: List<Task>) :
        DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldTasks.size
    }

    override fun getNewListSize(): Int {
        return newTasks.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks[oldItemPosition].id == newTasks[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldTasks[oldItemPosition]
        val newTask = newTasks[newItemPosition]
        return oldTask.title == newTask.title &&
                oldTask.priority == newTask.priority &&
                oldTask.dueBy == newTask.dueBy
    }
}
