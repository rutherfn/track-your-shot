package com.nicholas.rutherford.track.my.shot.app.center

import android.app.Application
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.microsoft.appcenter.AppCenter as MicrosoftAppCenter

class AppCenterImpl(private val application: Application, private val buildType: BuildType) : AppCenter {

    private fun generateAppCenterAppSecret(): String {
        return if (buildType.isDebug()) {
            "isDebug"
        } else if (buildType.isRelease()) {
            "isRelease"
        } else {
            "Dada"
        }
    }

    override fun start() {
        MicrosoftAppCenter.start(
            application, generateAppCenterAppSecret(),
            Analytics::class.java, Crashes::class.java
        )
    }
}
