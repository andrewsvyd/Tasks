package com.svyd.tasks.presentation.exception

import java.io.IOException

class IOExceptionDelegate(delegate: BaseExceptionDelegate<*>) : BaseExceptionDelegate<IOException>() {

    init {
        this.delegate = delegate
    }

    override fun handleException(exception: IOException) {
        view?.hideProgress()
        view?.showNoInternetConnectionError()
    }
}
