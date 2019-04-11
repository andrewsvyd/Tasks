package com.svyd.tasks.presentation.features.authorization

import android.os.Bundle
import com.svyd.tasks.data.networking.token.TokenManager
import com.svyd.tasks.data.repository.authorization.model.AuthorizationToken
import com.svyd.tasks.data.repository.authorization.model.UserCredentials
import com.svyd.tasks.presentation.base.BaseObserver
import com.svyd.tasks.presentation.base.BasePresenter
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.exception.BaseExceptionDelegate
import android.text.TextUtils
import com.svyd.tasks.presentation.base.SimpleObserver


class AuthorizationPresenter(delegate: BaseExceptionDelegate<out Throwable>,
                             private val signInInteractor: PostInteractor<UserCredentials, AuthorizationToken>,
                             private val signUpInteractor: PostInteractor<UserCredentials, AuthorizationToken>,
                             val tokenManager: TokenManager) : BasePresenter<AuthorizationContract.View>(delegate),
        AuthorizationContract.Presenter<AuthorizationContract.View> {

    var state = UserCredentials()

    override fun saveState(state: Bundle) {
        state.putParcelable(KEY_INSTANCE_STATE, this.state)
    }

    override fun restoreState(state: Bundle) {
        val credentials: UserCredentials? = state.getParcelable(KEY_INSTANCE_STATE)
        credentials?.let { userCredentials -> this.state = userCredentials }
    }

    override fun onNameChanged(name: String) {
        state.email = name
    }

    override fun onPasswordChanged(password: String) {
        state.password = password
    }

    override fun onSignIn() {
        if (validate()) {
            view?.showProgress()
            signInInteractor.execute(state, authObserver)
        }
    }

    override fun onSignUp() {
        if (validate()) {
            view?.showProgress()
            signUpInteractor.execute(state, authObserver)
        }
    }

    private fun isValidEmail(target: String?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    override fun onStop() {
        signInInteractor.unsubscribe()
        signUpInteractor.unsubscribe()
    }

    private val authObserver = object : SimpleObserver<AuthorizationToken>() {

        override fun onNext(t: AuthorizationToken) {
            tokenManager.saveToken(t.token)
        }

        override fun onCompleted() {
            view?.proceedAuthorization()
        }
    }

    private fun validate(): Boolean {
        if (!isValidEmail(state.email)) {
            onWrongEmail()
            return false
        }
        if (!validate(state.password)) {
            onWrongPassword()
            return false
        }
        return true
    }

    private fun validate(field: String?) = field != null && !field.isEmpty()

    private fun onWrongEmail() = view?.showEmailError()

    private fun onWrongPassword() = view?.showPasswordError()
}
