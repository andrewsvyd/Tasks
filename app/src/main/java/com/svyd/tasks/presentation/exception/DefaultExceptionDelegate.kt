package com.svyd.tasks.presentation.exception

class DefaultExceptionDelegate(delegate: BaseExceptionDelegate<*>) : BaseExceptionDelegate<Throwable>() {

    init {
        this.delegate = delegate
    }

    override fun handleException(exception: Throwable) {
        delegate?.onError(exception)
    }
}
