package com.nicholas.rutherford.track.my.shot.helper.network

import android.app.Application
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import java.net.InetAddress

class NetworkImpl(private val application: Application) : Network {

    override fun isDeviceConnectedToInternet(): Boolean {
        return try {
            !InetAddress.getByName(application.getString(StringsIds.googleCom)).equals(application.getString(StringsIds.empty))
        } catch (exception: Exception) {
            println(exception.stackTrace)
            false
        }
    }
}
