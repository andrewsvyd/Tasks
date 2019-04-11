package com.svyd.tasks.presentation.base

import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions

abstract class Interactor<out R> {

    private var subscription = Subscriptions.empty()

    fun execute(subscriber: Observer<in R>) {
        subscription = buildObserver()
                .subscribeOn(Schedulers.newThread())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }

    protected abstract fun buildObserver(): Observable<out R>

    fun unsubscribe() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }

}
