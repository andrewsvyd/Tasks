package com.svyd.tasks.presentation.features.tasks

import android.content.Intent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager

import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.google.android.material.snackbar.Snackbar
import com.svyd.tasks.R
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.base.BaseActivity
import com.svyd.tasks.presentation.base.BaseContract
import com.svyd.tasks.presentation.features.details.TaskDetailsActivity
import com.svyd.tasks.presentation.features.details.TaskDetailsActivity.Companion.RESULT_CODE_DELETED
import com.svyd.tasks.presentation.features.reminders.RemindersActivity
import com.svyd.tasks.presentation.features.reminders.RemindersActivity.Companion.RESULT_CODE_REMINDERS_DELETED
import com.svyd.tasks.presentation.features.settings.SettingsActivity
import com.svyd.tasks.presentation.features.settings.SettingsActivity.Companion.RESULT_SORT_CHANGED
import com.svyd.tasks.presentation.features.update.UpdateTaskActivity
import com.svyd.tasks.presentation.features.update.UpdateTaskActivity.Companion.RESULT_CREATED
import com.svyd.tasks.presentation.features.update.UpdateTaskActivity.Companion.RESULT_EDITED
import kotlinx.android.synthetic.main.activity_tasks.*

class TasksActivity : BaseActivity(), TasksContract.View, OnTaskClickListener {
    private lateinit var presenter: TasksContract.Presenter

    private lateinit var adapter: TasksAdapter
    override val contentView: Int
        get() = R.layout.activity_tasks

    override val toolbarId: Int
        get() = R.id.toolbar_AS

    override fun initializeUi() {
        adapter = TasksAdapter()
        adapter.setOnTaskClickListener(this)
        rvTasks_AT.layoutManager = LinearLayoutManager(this)
        rvTasks_AT.adapter = adapter

        fabCreateTask_AT.setOnClickListener { createTask() }
        ivReminders_AT.setOnClickListener { openReminders() }
        srTaskRefresh_AT.setOnRefreshListener { presenter.onDataSynced() }
    }

    private fun openReminders() {
        val intent = Intent(this, RemindersActivity::class.java)
        startActivityForResult(intent, REQUEST_DELETE_REMINDERS)
    }

    private fun createTask() {
        val intent = Intent(this, UpdateTaskActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_CREATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_CREATED -> onTaskCreated(data)
            RESULT_EDITED -> onTaskEdited(data)
            RESULT_CODE_DELETED -> onTaskDeleted(data)
            RESULT_CODE_REMINDERS_DELETED -> onRemindersDeleted(data)
            RESULT_SORT_CHANGED -> onSortChanged()
        }

        if (data != null &&
                data.hasExtra(BaseActivity.DATA_SYNCED) &&
                data.getBooleanExtra(BaseActivity.DATA_SYNCED, false)) {
            presenter.onDataSynced()
        }
    }

    private fun onSortChanged() {
        presenter.onSortChanged()
    }

    override fun onDataSynced() {
        presenter.onRefresh()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_settings) {
            navigateToSettings()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivityForResult(intent, SettingsActivity.REQUEST_SORT_SETTINGS)
    }

    private fun onRemindersDeleted(data: Intent?) {
        if (data != null && data.hasExtra(RemindersActivity.EXTRA_REMOVED_REMINDERS)) {
            val deletedReminders = data.getStringArrayListExtra(RemindersActivity.EXTRA_REMOVED_REMINDERS)
            presenter.onRemindersDeleted(deletedReminders)
        }
    }

    private fun onTaskCreated(data: Intent?) {
        if (data != null && data.hasExtra(UpdateTaskActivity.EXTRA_TASK)) {
            val task = data.getParcelableExtra<Task>(UpdateTaskActivity.EXTRA_TASK)
            presenter.onTaskCreated(task)
        }
    }

    private fun onTaskEdited(data: Intent?) {
        if (data != null && data.hasExtra(UpdateTaskActivity.EXTRA_TASK)) {
            val task = data.getParcelableExtra<Task>(UpdateTaskActivity.EXTRA_TASK)
            presenter.onTaskUpdated(task)
        }
    }

    private fun onTaskDeleted(data: Intent?) {
        if (data != null && data.hasExtra(TaskDetailsActivity.KEY_DELETED_TASK_ID)) {
            val id = data.getStringExtra(TaskDetailsActivity.KEY_DELETED_TASK_ID)
            presenter.onTaskDeleted(id)
        }
    }

    override fun showRefreshMessage() {
        showMessage(getString(R.string.message_refresh))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun unauthorised() {
        logOut()
    }

    override fun showTasks(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            showEmptyList()
        } else {
            setItemsWithDiff(tasks)
            showList()
        }
    }

    private fun showList() {
        llEmptyList_AT.visibility = View.GONE
        rvTasks_AT.visibility = View.VISIBLE
    }

    private fun showEmptyList() {
        rvTasks_AT.visibility = View.GONE
        llEmptyList_AT.visibility = View.VISIBLE
    }

    override fun navigateToTaskDetails(task: Task) {
        val intent = Intent(this, TaskDetailsActivity::class.java)
        intent.putExtra(TaskDetailsActivity.EXTRA_TASK, task)
        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }

    override fun updateList(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            showEmptyList()
        } else {
            setItemsWithDiff(tasks)
            showList()
        }
    }

    private fun setItemsWithDiff(tasks: List<Task>) {
        val callback = TasksDiffUtilCallback(adapter.items, tasks)
        val result = DiffUtil.calculateDiff(callback)

        adapter.setData(tasks)
        result.dispatchUpdatesTo(adapter)
    }

    override fun onTaskClick(id: String) {
        presenter.onTaskClick(id)
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    override fun initializePresenter() {
        presenter = TasksPresenterProvider().providePresenter()
        presenter.initialize(this)
    }

    override fun showMessage(message: String) {
        Snackbar.make(findViewById(R.id.clContainer_AT), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        srTaskRefresh_AT.isRefreshing = true
    }

    override fun hideProgress() {
        srTaskRefresh_AT.isRefreshing = false
    }

    companion object {

        const val REQUEST_CODE_DETAILS = 0
        const val REQUEST_CODE_CREATE = 1
        const val REQUEST_DELETE_REMINDERS = 9
    }
}
