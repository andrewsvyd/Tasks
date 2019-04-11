package com.svyd.tasks.presentation.base

import rx.Observer

abstract class BaseObserver<T>(private val presenter: BasePresenter<out BaseContract.View>) : Observer<T> {

    override fun onError(e: Throwable) {
        presenter.errorHandler.onError(e)
    }

    override fun onNext(t: T) {

    }

    override fun onCompleted() {

    }
}
