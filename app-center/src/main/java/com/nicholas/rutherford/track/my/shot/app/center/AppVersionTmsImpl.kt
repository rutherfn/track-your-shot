package com.nicholas.rutherford.track.my.shot.app.center

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

class AppVersionTmsImpl(private val application: Application) : AppCenterTms {

    override fun startAppCenter() {
        AppCenter.start(
            application, "{Your app secret here}",
            Analytics::class.java, Crashes::class.java
        )
    }
}