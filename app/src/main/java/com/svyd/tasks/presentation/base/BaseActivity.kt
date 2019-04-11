package com.svyd.tasks.presentation.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import com.svyd.tasks.presentation.TasksApplication
import com.svyd.tasks.presentation.network.DataSyncListener
import com.svyd.tasks.presentation.features.authorization.AuthorizationActivity
import com.svyd.tasks.util.find

/**
 * Base class for activities that handles initialization of MVP components.
 * Use [.getPresenter] to access the presenter which belongs to this view.
 *
 * @param <P> [BasePresenter] implementation that is needed for this view.
</P> */

abstract class BaseActivity : AppCompatActivity(), BaseContract.View, DataSyncListener {

    private var inputManager: InputMethodManager? = null
    protected var resultData: Intent = Intent()

    protected open val toolbarId: Int
        @IdRes
        get() = 0

    //endregion

    /**
     * A subclass of this [BaseActivity] has to provide a layout resource ID
     * for Content View. Used for [AppCompatActivity.setContentView]
     *
     * @return ID of [AppCompatActivity]'s layout resource.
     */

    @get:LayoutRes
    protected abstract val contentView: Int

    protected abstract fun getPresenter(): BaseContract.Presenter<*>

    //region Activity lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(contentView)
        initializeUi()
        initInputManager()
        initializeToolbar()
        initializePresenter()
    }

    protected abstract fun initializeUi()

    private fun initializeToolbar() {
        if (toolbarId != 0) {
            setSupportActionBar(find(toolbarId))
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    protected fun enableBackButton(enable: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(enable)
        supportActionBar?.setDisplayShowHomeEnabled(enable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initInputManager() {
        inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onStart() {
        super.onStart()
        getPresenter().onStart()
        TasksApplication.instance.networkChangeHandler.subscribeOnDataSync(this)
    }

    override fun onStop() {
        TasksApplication.instance.networkChangeHandler.unSubscribeFromDataSync(this)
        getPresenter().onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getPresenter().saveState(outState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        savedInstanceState?.let { state -> getPresenter().restoreState(state)}
    }

    //endregion


    /**
     * Create [BasePresenter] using [PresenterProvider]
     * and initialize it.
     */

    protected abstract fun initializePresenter()

    /**
     * @return new instance of particular [PresenterProvider]
     */

    override fun showMessage(message: String) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun showNoInternetConnectionError() =
            Toast.makeText(this, "Please, check your internet connection", Toast.LENGTH_SHORT).show()

    protected fun hideKeyboard(view: View) =
            inputManager?.hideSoftInputFromWindow(view.windowToken, 0)

    protected fun logOut() {
        TasksApplication.instance.tokenManager.clearToken()
        val intent = Intent(this, AuthorizationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onDataSynced() {
        resultData.putExtra(DATA_SYNCED, true)
        setResult(Activity.RESULT_OK, resultData)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    companion object {
        const val DATA_SYNCED = "data_synced"
    }
}
