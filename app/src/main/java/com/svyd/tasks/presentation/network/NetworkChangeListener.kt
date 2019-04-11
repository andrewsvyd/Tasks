package com.svyd.tasks.presentation.network

interface NetworkChangeListener {
    fun onNetworkChanged(isOnline: Boolean)
}
