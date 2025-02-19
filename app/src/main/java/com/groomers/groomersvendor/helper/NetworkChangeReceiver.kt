package com.groomers.groomersvendor.helper

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkChangeReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (context is ConnectivityListener) {
            (context as ConnectivityListener).onNetworkStatusChanged(isConnected)
        }
    }

    interface ConnectivityListener {
        fun onNetworkStatusChanged(isConnected: Boolean)
    }
}
