package com.nicholas.rutherford.track.your.shot.helper.network

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import android.net.Network as RealNetwork

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Implementation of [Network] that monitors the device's network connectivity status.
 *
 * Uses [ConnectivityManager] to register a network callback and emits connectivity changes
 * as a [Flow] of boolean values.
 *
 * @param connectivityManager System service used to observe network connectivity.
 */
class NetworkImpl(private val connectivityManager: ConnectivityManager) : Network {

    /**
     * A [Flow] emitting `true` when the device is connected to a validated network
     * and `false` when disconnected or network is lost.
     */
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

                override fun onLost(network: RealNetwork) {
                    super.onLost(network)
                    trySend(element = false)
                }

                override fun onAvailable(network: RealNetwork) {
                    super.onAvailable(network)
                    trySend(element = true)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            // Ensures the callback is unregistered when the flow collector is cancelled
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
}
