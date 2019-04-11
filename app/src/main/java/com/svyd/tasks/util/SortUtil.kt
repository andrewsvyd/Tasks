package com.svyd.tasks.util

import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.data.repository.tasks.model.TaskSort

object SortUtil {
    fun getComparator(sortRequest: SortRequest): Comparator<Task> {
        when (sortRequest.property) {
            TaskSort.PROPERTY_DATE -> {
                return if (sortRequest.ascending) {
                    compareBy { it.dueBy }
                } else {
                    compareByDescending { it.dueBy }
                }
            }
            TaskSort.PROPERTY_TITLE -> {
                return if (sortRequest.ascending) {
                    compareBy { it.title }
                } else {
                    compareByDescending { it.title }
                }
            }
            TaskSort.PROPERTY_PRIORITY -> {
                return if (sortRequest.ascending) {
                    compareBy { it.priority }
                } else {
                    compareByDescending { it.priority }
                }
            }
        }
        return compareBy { it.dueBy }
    }
}
