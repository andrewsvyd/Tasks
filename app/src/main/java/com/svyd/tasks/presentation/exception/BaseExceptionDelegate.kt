package com.svyd.tasks.presentation.exception

import com.svyd.tasks.presentation.base.BaseContract.View
import java.lang.reflect.ParameterizedType

abstract class BaseExceptionDelegate<T : Throwable> {

    var view: View? = null
        set(value) {
            field = value
            delegate?.view = value
        }
    var delegate: BaseExceptionDelegate<*>? = null
    private var genericExceptionClass: Class<T>? = null

    init {
        if (genericExceptionClass == null) {
            val pt = javaClass.genericSuperclass as ParameterizedType
            genericExceptionClass = pt.actualTypeArguments[0] as Class<T>
        }
    }

    fun onError(exception: Throwable) {
        if (isRightException(exception)) {
            handleException(exception as T)
        } else {
            delegate?.onError(exception)
        }
    }

    private fun isRightException(exception: Throwable): Boolean {
        return genericExceptionClass!!.isAssignableFrom(exception.javaClass)
    }

    protected abstract fun handleException(exception: T)
}
