package com.svyd.tasks.presentation.network

interface NetworkChangeHandler {
    fun subscribeOnDataSync(listener: DataSyncListener)
    fun unSubscribeFromDataSync(listener: DataSyncListener)
}
