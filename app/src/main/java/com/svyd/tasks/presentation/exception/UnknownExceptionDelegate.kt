package com.svyd.tasks.presentation.exception

class UnknownExceptionDelegate : BaseExceptionDelegate<Throwable>() {

    override fun handleException(exception: Throwable) {
        view?.hideProgress()
        view?.showMessage(exception.message ?: "")
    }
}
