package com.svyd.tasks.presentation.features.settings

import android.app.AlertDialog
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import com.svyd.tasks.R
import com.svyd.tasks.data.repository.tasks.model.TaskSort
import com.svyd.tasks.presentation.base.BaseActivity
import com.svyd.tasks.presentation.base.BaseContract
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), SettingsContract.View {

    private lateinit var presenter: SettingsContract.Presenter

    override val contentView: Int
        get() = R.layout.activity_settings

    override val toolbarId: Int
        get() = R.id.toolbar_AS


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        enableBackButton(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun initializeUi() {
        rlItemSortingBy_AS.setOnClickListener { showSortByOptions() }
        rlItemOrder_AS.setOnClickListener { showSortOrderOptions() }
        rlItemLogOut_AS.setOnClickListener { onLogOutClick() }
    }

    private fun onLogOutClick() {
        AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_logout)
                .setMessage(R.string.description_dialog_logout)
                .setPositiveButton(android.R.string.yes) { _, _ -> presenter.onLogOutClick() }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    private fun showSortOrderOptions() {
        val dropDownMenu = PopupMenu(applicationContext, rlItemOrder_AS, Gravity.END)
        dropDownMenu.menuInflater.inflate(R.menu.sort_order_options, dropDownMenu.menu)
        dropDownMenu.setOnMenuItemClickListener { this.onSortOrderOptionSelected(it) }
        dropDownMenu.show()
    }

    private fun showSortByOptions() {
        val dropDownMenu = PopupMenu(applicationContext, rlItemSortingBy_AS, Gravity.END)
        dropDownMenu.menuInflater.inflate(R.menu.sort_by_options, dropDownMenu.menu)
        dropDownMenu.setOnMenuItemClickListener { this.onSortByOptionSelected(it) }
        dropDownMenu.show()
    }

    private fun onSortOrderOptionSelected(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.item_sort_order_ascending -> presenter.onSortAscending(true)
            R.id.item_sort_order_descending -> presenter.onSortAscending(false)
        }
        return false
    }

    private fun onSortByOptionSelected(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.item_sort_by_date -> presenter.onSortBy(TaskSort.PROPERTY_DATE)
            R.id.item_sort_by_title -> presenter.onSortBy(TaskSort.PROPERTY_TITLE)
            R.id.item_sort_by_priority -> presenter.onSortBy(TaskSort.PROPERTY_PRIORITY)
        }
        return true
    }

    override fun setSortBy(sortBy: String) {
        when (sortBy) {
            TaskSort.PROPERTY_PRIORITY -> tvSortByPropertyDescription_AS.text = getString(R.string.item_sort_by_priority)
            TaskSort.PROPERTY_TITLE -> tvSortByPropertyDescription_AS.text = getString(R.string.item_sort_by_title)
            TaskSort.PROPERTY_DATE -> tvSortByPropertyDescription_AS.text = getString(R.string.item_sort_by_date)
        }
    }

    override fun setResultSortChanged() {
        setResult(RESULT_SORT_CHANGED, resultData)
    }

    override fun setSortOrder(ascending: Boolean) {
        if (ascending) {
            tvSortOrderDescription_AS.text = getString(R.string.item_sort_order_ascending)
        } else {
            tvSortOrderDescription_AS.text = getString(R.string.item_sort_order_descending)
        }
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    override fun unauthorised() {
        logOut()
    }

    override fun initializePresenter() {
        presenter = SettingsPresenterProvider().providePresenter()
        presenter.initialize(this)
    }

    companion object {
        const val RESULT_SORT_CHANGED = 321
        const val REQUEST_SORT_SETTINGS = 123
    }

}
