package com.svyd.tasks.presentation.base

import com.svyd.tasks.presentation.exception.BaseExceptionDelegate

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Base interface for all presenters.
 */
abstract class BasePresenter<V : BaseContract.View>(val errorHandler: BaseExceptionDelegate<*>)
    : BaseContract.Presenter<V> {

    var view: V? = null

    override fun onStart() {}

    override fun initialize(view: V) {
        this.view = view
        errorHandler.view = view
    }

    companion object {
        const val KEY_INSTANCE_STATE = "instance_state"
    }
}
