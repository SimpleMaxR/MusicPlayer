package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import android.widget.Toast

class NetworkChangeReceiver(private val context: Context) {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    fun startMonitoring() {
        connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                // 网络断开
                Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show()
                Log.i("Network","Toast OnLost")
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Toast.makeText(context, "网络连接", Toast.LENGTH_SHORT).show()
                Log.i("Network","Toast onAvailable")
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stopMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}