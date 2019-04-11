package com.svyd.tasks.presentation.network

interface NetworkChangeDetector {
    fun subscribe(listener: NetworkChangeListener)
    fun isOnline(): Boolean
}