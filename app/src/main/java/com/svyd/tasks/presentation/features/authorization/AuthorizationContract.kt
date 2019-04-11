package com.svyd.tasks.presentation.features.authorization

import com.svyd.tasks.presentation.base.BaseContract


interface AuthorizationContract {

    interface View : BaseContract.View {
        fun proceedAuthorization()
        fun showEmailError()
        fun showPasswordError()
    }

    interface Presenter<T : AuthorizationContract.View> : BaseContract.Presenter<T> {
        fun onNameChanged(name: String)
        fun onPasswordChanged(password: String)
        fun onSignIn()
        fun onSignUp()
    }
}