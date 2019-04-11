package com.svyd.tasks.presentation.network

import com.svyd.tasks.data.repository.tasks.datasource.CloudTasksDataSource
import com.svyd.tasks.data.repository.tasks.datasource.PersistentTasksDataSource
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.presentation.TasksApplication
import com.svyd.tasks.presentation.base.SimpleObserver
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AndroidNetworkChangeHandler(private val tasksPersistentDataSource: PersistentTasksDataSource,
                                  private val taskCloudDataSource: CloudTasksDataSource) : NetworkChangeHandler, NetworkChangeListener {
    var dataSyncListeners: ArrayList<DataSyncListener> = ArrayList()

    init {
        TasksApplication.instance.networkChangeDetector.subscribe(this)
    }

    override fun subscribeOnDataSync(listener: DataSyncListener) {
        dataSyncListeners.add(listener)
    }

    override fun onNetworkChanged(isOnline: Boolean) {
        if (isOnline) {
            syncData()
        }
    }

    override fun unSubscribeFromDataSync(listener: DataSyncListener) {
        dataSyncListeners.remove(listener)
    }
    private fun syncData() {
        syncDeletedTasks()
    }

    private fun syncDeletedTasks() {
        tasksPersistentDataSource.deletedTasks
                .flatMap { tasks -> taskCloudDataSource.deleteTasks(tasks) }
                .subscribeOn(Schedulers.newThread())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(SyncObserver(false, ::syncUpdatedTasks))
    }

    private fun syncUpdatedTasks(updated: Boolean) {
        tasksPersistentDataSource.updatedTasks
                .flatMap { tasks -> taskCloudDataSource.updateTasks(tasks) }
                .subscribeOn(Schedulers.newThread())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(SyncObserver(updated, ::syncCreatedTasks))
    }

    private fun syncCreatedTasks(updated: Boolean) {
        tasksPersistentDataSource.createdTasks
                .flatMap { tasks -> taskCloudDataSource.createTasks(tasks) }
                .subscribeOn(Schedulers.newThread())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(SyncObserver(updated, ::onSyncCompleted))
    }

    private fun onSyncCompleted(updated: Boolean) {
        if (updated) {
            dataSyncListeners.forEach{ listener -> listener.onDataSynced()}
        }
    }

    inner class SyncObserver(private var updated: Boolean,
                             val doOnCompleted: (updated: Boolean) -> Unit) : SimpleObserver<Task>() {

        override fun onNext(t: Task) {
            updated = true
        }

        override fun onCompleted() {
            doOnCompleted(updated)
        }
    }
}
