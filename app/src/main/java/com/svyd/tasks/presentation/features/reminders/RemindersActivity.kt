package com.svyd.tasks.presentation.features.reminders

import android.view.Menu
import android.view.View

import com.svyd.tasks.R
import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.presentation.base.BaseActivity
import com.svyd.tasks.presentation.base.BaseContract
import com.svyd.tasks.presentation.features.reminders.list.ItemDeletedListener
import com.svyd.tasks.presentation.features.reminders.list.RemindersAdapter
import com.svyd.tasks.presentation.features.reminders.list.SwipeToDeleteCallback

import java.util.ArrayList

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_reminders.*

class RemindersActivity : BaseActivity(), RemindersContract.View, ItemDeletedListener {
    private lateinit var presenter: RemindersContract.Presenter

    private lateinit var adapter: RemindersAdapter

    override val contentView: Int
        get() = R.layout.activity_reminders

    override val toolbarId: Int
        get() = R.id.toolbar_AR

    override fun initializeUi() {

        adapter = RemindersAdapter(this)
        adapter.setDeletionListener(this)
        rvReminders_AR.layoutManager = LinearLayoutManager(this)
        rvReminders_AR.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(rvReminders_AR)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        enableBackButton(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun getPresenter(): BaseContract.Presenter<RemindersContract.View> {
        return presenter
    }

    override fun initializePresenter() {
        presenter = RemindersPresenterProvider().providePresenter()
        presenter.initialize(this)
    }

    override fun showReminders(reminders: List<Reminder>) {
        if (reminders.isEmpty()) {
            showEmptyList()
        } else {
            adapter.setData(reminders)
            showList()
        }
    }

    private fun showList() {
        llEmptyList_AR.visibility = View.GONE
        rvReminders_AR.visibility = View.VISIBLE
    }

    override fun undoDeletion(deletedPosition: Int) {
        adapter.undoDeletion(deletedPosition)
    }

    override fun setDeletedReminders(removedReminders: ArrayList<String>) {
        resultData.putStringArrayListExtra(EXTRA_REMOVED_REMINDERS, removedReminders)
        setResult(RESULT_CODE_REMINDERS_DELETED, resultData)
    }

    override fun showEmptyList() {
        rvReminders_AR.visibility = View.GONE
        llEmptyList_AR.visibility = View.VISIBLE
    }

    override fun onItemDeleted(position: Int) {
        presenter.onReminderDelete(position)
    }

    override fun unauthorised() {

    }

    companion object {

        const val RESULT_CODE_REMINDERS_DELETED = 8
        const val EXTRA_REMOVED_REMINDERS = "removed_reminders"
    }
}
