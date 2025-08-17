package com.nicholas.rutherford.track.your.shot.helper.network

import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Abstraction for monitoring network connectivity status.
 *
 * Implementations of this interface provide a [Flow] that emits connectivity state updates.
 */
interface Network {

    /**
     * A [Flow] that emits the current network connectivity status.
     *
     * - Emits `true` when the device is connected to a network.
     * - Emits `false` when the device is disconnected or the network becomes unavailable.
     */
    val isConnected: Flow<Boolean>
}
