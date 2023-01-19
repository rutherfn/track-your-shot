package com.nicholas.rutherford.track.my.shot.helper.network

interface Network {
    suspend fun isDeviceConnectedToInternet(): Boolean
}
