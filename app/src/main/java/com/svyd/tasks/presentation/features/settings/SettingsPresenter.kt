package com.svyd.tasks.presentation.features.settings

import android.os.Bundle
import com.svyd.tasks.data.repository.tasks.model.SortRequest
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.BaseObserver
import com.svyd.tasks.presentation.base.BasePresenter
import com.svyd.tasks.presentation.base.Interactor
import com.svyd.tasks.presentation.base.sorting.SortingPreference
import com.svyd.tasks.presentation.exception.BaseExceptionDelegate

class SettingsPresenter internal constructor(private val sortingPreference: SortingPreference,
                                             private val logOutInteractor: Interactor<Unit>,
                                             delegate: BaseExceptionDelegate<*>) :
        BasePresenter<SettingsContract.View>(delegate),
        SettingsContract.Presenter {

    private lateinit var sortRequest: SortRequest

    override fun initialize(view: SettingsContract.View) {
        super.initialize(view)
        sortRequest = sortingPreference.sorting
        view.setSortBy(sortRequest.property)
        view.setSortOrder(sortRequest.ascending)
    }

    override fun onStop() {
        logOutInteractor.unsubscribe()
    }

    override fun restoreState(state: Bundle) {
        val savedSort: SortRequest? = state.getParcelable(BasePresenter.KEY_INSTANCE_STATE)
        if (savedSort != null) sortRequest = savedSort
    }

    override fun saveState(state: Bundle) {
        state.putParcelable(KEY_INSTANCE_STATE, sortRequest)
    }

    override fun onLogOutClick() {
        sortingPreference.clear()
        logOutInteractor.execute(LogOutObserver(this))
    }

    override fun onSortBy(property: String) {
        sortRequest.property = property
        view?.setSortBy(property)
        update()
    }

    override fun onSortAscending(ascending: Boolean) {
        sortRequest.ascending = ascending
        view?.setSortOrder(ascending)
        update()
    }

    fun update() {
        sortingPreference.updateSortingPreference(sortRequest)
        view?.setResultSortChanged()
    }

    internal inner class LogOutObserver(presenter: BasePresenter<*>) : BaseObserver<Unit>(presenter) {
        override fun onCompleted() {
            view?.unauthorised()
        }
    }

}