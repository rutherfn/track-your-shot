package com.nicholas.rutherford.track.your.shot.feature.splash.declaredshotsjson

import android.app.Application
import com.nicholas.rutherford.track.your.shot.feature.splash.RawIds

class DeclaredShotsJsonImpl(private val application: Application) : DeclaredShotsJson {
    override fun fetchJsonDeclaredShots(): String {
        val inputStream = application.resources.openRawResource(RawIds.basketballShotsJson)
        return inputStream.bufferedReader().use { reader -> reader.readText() }
    }

    override fun writeToJsonDeclaredShots() {
    }
}
