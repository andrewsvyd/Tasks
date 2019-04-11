package com.svyd.tasks.presentation.features.authorization

import android.animation.LayoutTransition
import android.content.Intent
import android.view.View

import com.svyd.tasks.R
import com.svyd.tasks.presentation.base.AbstractTextWatcher
import com.svyd.tasks.presentation.base.BaseActivity
import com.svyd.tasks.presentation.base.BaseContract
import com.svyd.tasks.presentation.features.tasks.TasksActivity
import kotlinx.android.synthetic.main.activity_authorization.*

class AuthorizationActivity : BaseActivity(), AuthorizationContract.View {

    override fun initializePresenter() {
        presenter = AuthorizationPresenterProvider().providePresenter()
        presenter.initialize(this)
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    lateinit var presenter: AuthorizationPresenter

    override val contentView: Int
        get() = R.layout.activity_authorization

    override fun initializeUi() {
        enableLayoutChangingTransitions()
        etName_AA.addTextChangedListener(object : AbstractTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                presenter.onNameChanged(s.toString())
                hideEmailError()
            }
        })

        etPassword_AA.addTextChangedListener(object : AbstractTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                presenter.onPasswordChanged(s.toString())
                hidePasswordError()
            }
        })

        findViewById<View>(R.id.tvLogIn_AA).setOnClickListener { onLogInClick() }
        rlLoginContainer_AA.visibility = View.VISIBLE
    }

    private fun hidePasswordError() {
        tilPassword_AA.error = null
        tilPassword_AA.isErrorEnabled = false
    }

    private fun hideEmailError() {
        tilEmail_AA.error = null
        tilEmail_AA.isErrorEnabled = false
    }

    private fun onLogInClick() {
        if (swLoginRegister_AA.isChecked) {
            presenter.onSignUp()
        } else {
            presenter.onSignIn()
        }
        hideKeyboard(etName_AA)
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    private fun enableLayoutChangingTransitions() {
        rlLoginContainer_AA.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        rlLoginContainer_AA.layoutTransition
                .enableTransitionType(LayoutTransition.CHANGING)
    }

    override fun unauthorised() = showMessage(getString(R.string.invalid_credentials))

    override fun proceedAuthorization() {
        val intent = Intent(this, TasksActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun showEmailError() {
        tilEmail_AA.isErrorEnabled = true
        tilEmail_AA.error = getString(R.string.error_email)
    }

    override fun showPasswordError() {
        tilPassword_AA.isErrorEnabled = true
        tilPassword_AA.error = getString(R.string.error_password)
    }
}
