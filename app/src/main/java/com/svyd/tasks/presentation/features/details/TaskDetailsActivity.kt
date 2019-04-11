package com.svyd.tasks.presentation.features.details

import android.app.AlertDialog
import android.content.Intent
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem

import com.svyd.tasks.R
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.data.repository.tasks.model.TaskPriority.HIGH
import com.svyd.tasks.data.repository.tasks.model.TaskPriority.LOW
import com.svyd.tasks.data.repository.tasks.model.TaskPriority.MEDIUM
import com.svyd.tasks.presentation.base.BaseActivity
import com.svyd.tasks.presentation.base.BaseContract
import com.svyd.tasks.presentation.features.update.UpdateTaskActivity
import com.svyd.tasks.util.ReminderUtil
import kotlinx.android.synthetic.main.activity_task_details.*

import java.util.Calendar
import java.util.Locale

class TaskDetailsActivity : BaseActivity(), TaskDetailsContract.View {

    lateinit var presenter: TaskDetailsContract.Presenter

    override val contentView: Int
        get() = R.layout.activity_task_details

    override val toolbarId: Int
        get() = R.id.toolbar_ATD

    override fun initializeUi() {

        fabDeleteTask_ATD.setOnClickListener { presenter.onDeleteClick() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_task_details, menu)
        enableBackButton(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == UpdateTaskActivity.RESULT_EDITED &&
                requestCode == REQUEST_EDIT_TASK &&
                data != null) {
            resultData = data
            onTaskEdited(data)
        }
    }

    private fun onTaskEdited(data: Intent?) {
        if (data != null && data.hasExtra(UpdateTaskActivity.EXTRA_TASK)) {
            val task = data.getParcelableExtra<Task>(UpdateTaskActivity.EXTRA_TASK)
            setResult(UpdateTaskActivity.RESULT_EDITED, data)
            presenter.onTaskEdited(task)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_edit) {
            presenter.onEditClick()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun unauthorised() {
        logOut()
    }

    override fun setText(title: String) {
        tvText_ATD.text = title
    }

    override fun setPriority(priority: String) {
        when (priority) {
            HIGH -> {
                etPriority_ATD.setText(getString(R.string.priority_high))
                etPriority_ATD.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getDrawable(R.drawable.vector_priority_high), null)
            }
            MEDIUM -> {
                etPriority_ATD.setText(getString(R.string.priority_medium))
                etPriority_ATD.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getDrawable(R.drawable.vector_priority_medium), null)
            }
            LOW -> {
                etPriority_ATD.setText(getString(R.string.priority_low))
                etPriority_ATD.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getDrawable(R.drawable.vector_priority_low), null)
            }
        }
    }

    override fun setDate(date: Long) {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = date
        etDueTo_ATD.setText(DateFormat.format(TIME_FORMAT, cal).toString())
    }

    override fun showDeletePrompt() {
        showDeleteDialog()
    }

    override fun navigateBackWithDeletion(id: String) {
        resultData.putExtra(KEY_DELETED_TASK_ID, id)
        setResult(RESULT_CODE_DELETED, resultData)
        onBackPressed()
    }

    override fun navigateToEditTask(task: Task) {
        val intent = Intent(this, UpdateTaskActivity::class.java)
        intent.putExtra(UpdateTaskActivity.EXTRA_TASK, task)
        startActivityForResult(intent, REQUEST_EDIT_TASK)
    }

    override fun setReminder(time: Int) {
        etReminder_ATD.setText(ReminderUtil.getReminderTextForTime(time))
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_delete_task)
                .setMessage(R.string.description_dialog_delete_task)
                .setPositiveButton(android.R.string.yes) { _, _ -> presenter.onDeleteConfirm() }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    override fun initializePresenter() {
        presenter = TaskDetailsPresenterProvider().providePresenter(intent.extras!!)
        presenter.initialize(this)
    }

    companion object {

        const val EXTRA_TASK = "extra_task_details"
        const val KEY_DELETED_TASK_ID = "key_deleted_task_id"

        const val RESULT_CODE_DELETED = 2

        private const val REQUEST_EDIT_TASK = 3
        private const val TIME_FORMAT = "dd/MM/yyyy HH:mm"
    }
}
