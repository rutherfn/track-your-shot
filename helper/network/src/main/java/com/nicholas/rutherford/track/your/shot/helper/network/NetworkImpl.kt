package com.nicholas.rutherford.track.your.shot.helper.network

class NetworkImpl : Network {

    override suspend fun isDeviceConnectedToInternet(): Boolean = true
}
