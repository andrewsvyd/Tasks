package com.svyd.tasks.presentation.base.sorting

import com.svyd.tasks.data.repository.tasks.model.SortRequest

interface SortingPreference {
    val sorting: SortRequest
    fun updateSortingPreference(sortRequest: SortRequest)
    fun clear()
}