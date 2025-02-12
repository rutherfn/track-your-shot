package com.nicholas.rutherford.track.your.shot.helper.network

import kotlinx.coroutines.flow.Flow

interface Network {
    val isConnected: Flow<Boolean>
}
