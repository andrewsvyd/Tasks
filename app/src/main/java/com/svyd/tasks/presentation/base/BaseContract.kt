package com.svyd.tasks.presentation.base

import android.os.Bundle
import rx.Subscription


interface BaseContract {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showMessage(message: String)
        fun showNoInternetConnectionError()
        fun unauthorised()
    }

    interface Presenter<in V: View> {
        fun onStart()
        fun initialize(view: V)
        fun onStop()
        fun restoreState(state: Bundle)
        fun saveState(state: Bundle)
    }
}