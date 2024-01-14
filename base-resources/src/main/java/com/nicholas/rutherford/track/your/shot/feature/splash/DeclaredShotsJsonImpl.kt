package com.nicholas.rutherford.track.your.shot.feature.splash

import android.app.Application

class DeclaredShotsJsonImpl(private val application: Application) : DeclaredShotsJson {
    override fun fetchJsonDeclaredShots(): String {
        val inputStream = application.resources.openRawResource(RawIds.basketballShotsJson)
        return inputStream.bufferedReader().use { reader -> reader.readText() }
    }

    override fun writeToJsonDeclaredShots() {
    }
}
