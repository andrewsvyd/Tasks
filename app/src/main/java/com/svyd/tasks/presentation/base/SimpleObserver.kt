package com.svyd.tasks.presentation.base

import rx.Observer

abstract class SimpleObserver<T> : Observer<T> {

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {

    }

    override fun onCompleted() {

    }
}
