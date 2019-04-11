package com.svyd.tasks.presentation.features.settings

import com.svyd.tasks.presentation.base.BaseContract

interface SettingsContract {
    interface View: BaseContract.View {
        fun setSortBy(sortBy: String)
        fun setSortOrder(ascending: Boolean)
        fun setResultSortChanged()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun onLogOutClick()
        fun onSortBy(property: String)
        fun onSortAscending(ascending: Boolean)
    }
}
