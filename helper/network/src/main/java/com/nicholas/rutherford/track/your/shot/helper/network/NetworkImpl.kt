package com.nicholas.rutherford.track.your.shot.helper.network

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkImpl(private val connectivityManager: ConnectivityManager) : Network {

    override val isConnected: Flow<Boolean>
        get() = callbackFlow {
            val callback = object : NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: android.net.Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val connected = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                    trySend(element = connected)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(element = false)
                }

                override fun onLost(network: android.net.Network) {
                    super.onLost(network)
                    trySend(element = false)
                }

                override fun onAvailable(network: android.net.Network) {
                    super.onAvailable(network)
                    trySend(element = true)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
}
