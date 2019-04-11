package com.svyd.tasks.presentation.base.sorting

import android.content.SharedPreferences
import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.TaskSort

class SharedSortingPreference(private val preferences: SharedPreferences) : SortingPreference {

    override fun clear() {
        preferences
                .edit()
                .clear()
                .apply()
    }

    override val sorting: SortRequest
        get() = SortRequest(preferences.getString(KEY_SORTING_PROPERTY, TaskSort.PROPERTY_DATE)!!,
                preferences.getBoolean(KEY_SORTING_ASCENDING, true))

    override fun updateSortingPreference(sortRequest: SortRequest) {
        preferences
                .edit()
                .putString(KEY_SORTING_PROPERTY, sortRequest.property)
                .apply()

        preferences
                .edit()
                .putBoolean(KEY_SORTING_ASCENDING, sortRequest.ascending)
                .apply()
    }

    companion object {
        private const val KEY_SORTING_PROPERTY = "sorting_property"
        private const val KEY_SORTING_ASCENDING = "sorting_ascending"
    }

}
