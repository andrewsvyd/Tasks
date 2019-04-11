package com.svyd.tasks.presentation.features.update

import android.animation.LayoutTransition
import android.app.DatePickerDialog

import android.app.TimePickerDialog
import android.content.Intent
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu

import com.svyd.tasks.R
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.data.repository.tasks.model.TaskPriority
import com.svyd.tasks.presentation.base.AbstractTextWatcher
import com.svyd.tasks.presentation.base.BaseActivity
import com.svyd.tasks.presentation.base.BaseContract
import com.svyd.tasks.util.ReminderUtil
import kotlinx.android.synthetic.main.activity_update_task.*

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UpdateTaskActivity : BaseActivity(), UpdateTaskContract.View {

    private lateinit var presenter: UpdateTaskContract.Presenter

    override val toolbarId: Int
        get() = R.id.toolbar_AET

    override val contentView: Int
        get() = R.layout.activity_update_task

    override fun initializeUi() {
        enableLayoutChangingTransitions()

        rgPriority_AUT.setOnCheckedChangeListener { _, checkedId -> onPriorityChanged(checkedId) }

        etDueTo_AUT.setOnClickListener { onDateClick() }
        etReminder_AUT.setOnClickListener { onReminderClick() }

        etTitle_AUT.addTextChangedListener(object : AbstractTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                onTitleTextChanged(s.toString())
            }
        })
    }

    private fun onReminderClick() {
        hideReminderError()
        val dropDownMenu = PopupMenu(applicationContext, etReminder_AUT, Gravity.END)
        dropDownMenu.menuInflater.inflate(R.menu.reminder_time_options, dropDownMenu.menu)
        dropDownMenu.setOnMenuItemClickListener { this.onReminderOptionSelected(it) }
        dropDownMenu.show()
    }

    private fun hideReminderError() {
        tilReminder_AUT.error = null
        tilReminder_AUT.isErrorEnabled = false
    }

    private fun onReminderOptionSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.item_reminder_disabled -> presenter.onReminderTimeChanged(ReminderUtil.getTimeForReminder(ReminderUtil.REMINDER_DISABLED))
            R.id.item_reminder_five_minutes -> presenter.onReminderTimeChanged(ReminderUtil.getTimeForReminder(ReminderUtil.REMINDER_5_MINUTES))
            R.id.item_reminder_ten_minutes -> presenter.onReminderTimeChanged(ReminderUtil.getTimeForReminder(ReminderUtil.REMINDER_10_MINUTES))
            R.id.item_reminder_thirty_minutes -> presenter.onReminderTimeChanged(ReminderUtil.getTimeForReminder(ReminderUtil.REMINDER_30_MINUTES))
            R.id.item_reminder_hour -> presenter.onReminderTimeChanged(ReminderUtil.getTimeForReminder(ReminderUtil.REMINDER_1_HOUR))
        }
        return true
    }

    private fun enableLayoutChangingTransitions() {
        rlContainer_AUT.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        rlContainer_AUT.layoutTransition
                .enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun onTitleTextChanged(title: String) {
        presenter.onTitleChanged(title)
        hideTitleError()
    }

    private fun hideTitleError() {
        tilTitle_AUT.error = null
        tilTitle_AUT.isErrorEnabled = false
    }

    private fun onDateClick() {
        presenter.onDateClick()
        hideDateError()
    }

    private fun hideDateError() {
        tilDueTo_AUT.error = null
        tilDueTo_AUT.isErrorEnabled = false
    }

    private fun onPriorityChanged(checkedId: Int) {
        when (checkedId) {
            R.id.rbHigh_AET -> presenter.onPriorityChanged(TaskPriority.HIGH)
            R.id.rbMedium_AET -> presenter.onPriorityChanged(TaskPriority.MEDIUM)
            R.id.rbLow_AET -> presenter.onPriorityChanged(TaskPriority.LOW)
        }
    }

    private fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    presenter.onDateChanged(calendar.time.time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true)
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_create_task, menu)
        enableBackButton(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_save -> {
                presenter.onSaveClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun unauthorised() {
        logOut()
    }

    override fun setCreateTitle() {
        tvTitle_AET.setText(R.string.title_create_task)
    }

    override fun setEditTitle() {
        tvTitle_AET.setText(R.string.title_edit_task)
    }

    override fun showDatePickerWithCurrentDate() {
        val calendar = Calendar.getInstance()
        showDatePicker(calendar)
    }

    private fun showDatePicker(calendar: Calendar) {
        DatePickerDialog(
                this,
                { _, year, month, dayOfMonth -> onDateSelected(year, month, dayOfMonth) },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show()
    }

    override fun showDatePickerWithDate(date: Long) {
        val calendar = Calendar.getInstance()
        calendar.time = Date(date)
        showDatePicker(calendar)
    }

    override fun showDateInPastError() {
        tilDueTo_AUT.isErrorEnabled = true
        tilDueTo_AUT.error = getString(R.string.error_date_in_past)
    }

    override fun setDate(date: Long) {
        val calendar = Calendar.getInstance()
        calendar.time = Date(date)
        val format = SimpleDateFormat(TIME_FORMAT, Locale.US)
        etDueTo_AUT.setText(format.format(calendar.time))
    }

    override fun setReminder(date: Int) {
        etReminder_AUT.setText(ReminderUtil.getReminderTextForTime(date))
    }

    override fun showEmptyTextError() {
        tilTitle_AUT.isErrorEnabled = true
        tilTitle_AUT.error = getString(R.string.error_empty_text)
    }

    override fun setPriority(priority: String) {
        when (priority) {
            TaskPriority.HIGH -> rgPriority_AUT.check(R.id.rbHigh_AET)
            TaskPriority.MEDIUM -> rgPriority_AUT.check(R.id.rbMedium_AET)
            TaskPriority.LOW -> rgPriority_AUT.check(R.id.rbLow_AET)
        }
    }

    override fun setText(title: String) {
        etTitle_AUT.setText(title)
    }

    override fun navigateBackWithEditedResult(task: Task) {
        val intent = Intent()
        intent.putExtra(EXTRA_TASK, task)
        setResult(RESULT_EDITED, intent)
        onBackPressed()
    }

    override fun navigateBackWithCreatedResult(task: Task) {
        val intent = Intent()
        intent.putExtra(EXTRA_TASK, task)
        setResult(RESULT_CREATED, intent)
        onBackPressed()
    }

    override fun showReminderInPastError() {
        tilReminder_AUT.isErrorEnabled = false
        tilReminder_AUT.error = getString(R.string.error_reminder_in_past)
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    override fun initializePresenter() {
        presenter = UpdateTaskPresenterProvider().providePresenter(intent.extras)
        presenter.initialize(this)
    }

    companion object {

        const val EXTRA_TASK = "key_task_update"

        const val RESULT_EDITED = 3
        const val RESULT_CREATED = 4
        private const val TIME_FORMAT = "dd/MM/yyyy HH:mm"
    }
}
