package com.svyd.tasks.presentation.exception

import retrofit2.adapter.rxjava.HttpException

class HttpExceptionDelegate(delegate: BaseExceptionDelegate<*>) : BaseExceptionDelegate<HttpException>() {

    companion object {
        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
    }

    init {
        this.delegate = delegate
    }

    override fun handleException(exception: HttpException) {
        view?.hideProgress()
        if (exception.code() == UNAUTHORIZED || exception.code() == FORBIDDEN) {
            view?.unauthorised()
        } else {
            view?.showMessage(exception.response().message())
        }
    }
}
