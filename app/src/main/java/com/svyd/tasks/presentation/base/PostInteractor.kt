package com.svyd.tasks.presentation.base

import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions

abstract class PostInteractor<D, R> {

    private var subscription = Subscriptions.empty()

    fun execute(data: D, subscriber: Observer<R>) {
        subscription = buildPostObservable(data)
                .subscribeOn(Schedulers.newThread())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }

    protected abstract fun buildPostObservable(data: D): Observable<R>

    fun unsubscribe() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }

}
