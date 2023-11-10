package com.nicholas.rutherford.track.your.shot.helper.network

interface Network {
    suspend fun isDeviceConnectedToInternet(): Boolean
}
