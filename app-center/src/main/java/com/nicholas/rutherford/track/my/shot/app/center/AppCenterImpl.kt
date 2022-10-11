package com.nicholas.rutherford.track.my.shot.app.center

import android.app.Application
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.microsoft.appcenter.AppCenter as MicrosoftAppCenter

// TODO https://trello.com/c/0DbVGVAQ/37-figure-out-a-way-to-hide-app-center-api-keys
const val DEBUG_APP_CENTER_SECRET = "f10e0fb2-e4b7-4d56-92db-08db35433dfa"
const val RELEASE_APP_CENTER_SECRET = "babe5ef0-7d3f-470c-b396-ecd83744e35"
const val STAGE_APP_CENTER_SECRET = "b5f5d03d-27a9-4186-874a-5c85e184f0bc"

class AppCenterImpl(private val application: Application, private val buildType: BuildType) : AppCenter {

    internal fun generateAppCenterAppSecret(): String {
        return if (buildType.isDebug()) {
            DEBUG_APP_CENTER_SECRET
        } else if (buildType.isRelease()) {
            RELEASE_APP_CENTER_SECRET
        } else {
            STAGE_APP_CENTER_SECRET
        }
    }

    override fun start() {
        MicrosoftAppCenter.start(
            application, generateAppCenterAppSecret(),
            Analytics::class.java, Crashes::class.java
        )
    }
}
