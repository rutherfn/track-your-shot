package com.nicholas.rutherford.track.your.shot.helper.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

const val HOST_NAME = "8.8.8.8"
const val PORT = 53
const val TIMEOUT_MS = 1500

class NetworkImpl(private val scope: CoroutineScope) : Network {

    override suspend fun isDeviceConnectedToInternet(): Boolean = scope.async {
        try {
            val sock = Socket()
            return@async runCatching {
                sock.connect(InetSocketAddress(HOST_NAME, PORT), TIMEOUT_MS)
                sock.close()
            }.isSuccess
        } catch (e: IOException) {
            return@async false
        }
    }.await()
}
