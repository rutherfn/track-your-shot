package com.nicholas.rutherford.track.my.shot.app.center

import android.app.Application
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.AppCenter as MicrosoftAppCenter

const val BUILD_TYPE_DEBUG = "debug"
const val BUILD_TYPE_RELEASE = "release"

class AppCenterImpl(private val application: Application) : AppCenter {

    private fun generateAppCenterAppSecret(): String {
        return when (BuildConfig.BUILD_TYPE) {
            BUILD_TYPE_DEBUG -> {
                "22"
            }
            BUILD_TYPE_RELEASE -> {
                "11"
            }
            else -> {
                "44"
            }
        }
    }

    override fun start() {
        MicrosoftAppCenter.start(
            application, generateAppCenterAppSecret(),
            Analytics::class.java, Crashes::class.java
        )
    }
}
