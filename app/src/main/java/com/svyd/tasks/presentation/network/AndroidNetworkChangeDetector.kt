package com.svyd.tasks.presentation.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

class AndroidNetworkChangeDetector(private val context: Context):
        NetworkChangeDetector, BroadcastReceiver() {

    private val subscribers: ArrayList<NetworkChangeListener> = ArrayList()

    init {
        context.registerReceiver(this, IntentFilter(CONNECTIVITY_FILTER))
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        onNetworkChanged(isOnline())
    }

    private fun onNetworkChanged(online: Boolean) {
        subscribers.forEach{ listener -> listener.onNetworkChanged(online)}
    }

    override fun subscribe(listener: NetworkChangeListener) {
        subscribers.add(listener)
    }

    override fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
    companion object {
        const val CONNECTIVITY_FILTER = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}
