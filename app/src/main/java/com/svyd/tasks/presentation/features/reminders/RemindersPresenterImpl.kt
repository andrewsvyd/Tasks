package com.svyd.tasks.presentation.features.reminders

import android.os.Bundle

import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.data.repository.reminders.model.Reminders
import com.svyd.tasks.presentation.base.BaseContract
import com.svyd.tasks.presentation.base.BaseObserver
import com.svyd.tasks.presentation.base.BasePresenter
import com.svyd.tasks.presentation.base.Interactor
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.exception.BaseExceptionDelegate

import java.util.ArrayList

class RemindersPresenterImpl(errorHandler: BaseExceptionDelegate<*>,
                             private val remindersInteractor: Interactor<MutableList<Reminder>>,
                             private val deleteReminderInteractor: PostInteractor<Reminder, Reminder>) :
        BasePresenter<RemindersContract.View>(errorHandler),
        RemindersContract.Presenter {

    private var reminders: Reminders? = null

    private val removedReminders = ArrayList<String>()

    override fun initialize(view: RemindersContract.View) {
        super.initialize(view)
        remindersInteractor.execute(RemindersObserver(this))
    }

    override fun onReminderDelete(position: Int) {
        val reminder = reminders?.reminders?.get(position)

        reminder?.let { deleteReminderInteractor.execute(reminder,
                DeleteReminderObserver(this, position, reminder))
        }

        reminders?.reminders?.removeAt(position)
        if (reminders?.reminders?.isEmpty()!!) {
            view?.showEmptyList()
        }
    }

    override fun onStop() {
        remindersInteractor.unsubscribe()
        deleteReminderInteractor.unsubscribe()
    }

    override fun restoreState(state: Bundle) {
        reminders = state.getParcelable(BasePresenter.KEY_INSTANCE_STATE)
    }

    override fun saveState(state: Bundle) {
        state.putParcelable(BasePresenter.KEY_INSTANCE_STATE, reminders)
    }

    private fun handleReminders(reminders: MutableList<Reminder>) {
        this.reminders = Reminders(reminders)
        view?.showReminders(reminders)
    }

    private inner class RemindersObserver internal constructor(presenter: BasePresenter<out BaseContract.View>) :
            BaseObserver<MutableList<Reminder>>(presenter) {

        override fun onNext(t: MutableList<Reminder>) {
            handleReminders(t)
        }
    }

    private inner class DeleteReminderObserver internal constructor(presenter: BasePresenter<out BaseContract.View>,
                                                                    private val deletedPosition: Int,
                                                                    private val deletedReminder: Reminder) :
            BaseObserver<Reminder>(presenter) {

        override fun onError(e: Throwable) {
            super.onError(e)
            reminders?.reminders?.add(deletedPosition, deletedReminder)
            view?.undoDeletion(deletedPosition)
        }

        override fun onNext(t: Reminder) {
            removedReminders.add(t.taskId)
            view?.setDeletedReminders(removedReminders)
        }
    }
}
