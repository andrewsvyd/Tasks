package com.svyd.tasks.presentation.base

import com.svyd.tasks.presentation.exception.BaseExceptionDelegate
import com.svyd.tasks.presentation.exception.DefaultExceptionDelegate
import com.svyd.tasks.presentation.exception.HttpExceptionDelegate
import com.svyd.tasks.presentation.exception.IOExceptionDelegate
import com.svyd.tasks.presentation.exception.UnknownExceptionDelegate

abstract class PresenterProvider {
    protected fun provideExceptionDelegate(): BaseExceptionDelegate<*> {
        val unknownExceptionDelegate = UnknownExceptionDelegate()
        val ioExceptionDelegate = IOExceptionDelegate(unknownExceptionDelegate)
        val httpExceptionDelegate = HttpExceptionDelegate(ioExceptionDelegate)

        return DefaultExceptionDelegate(httpExceptionDelegate)
    }
}
