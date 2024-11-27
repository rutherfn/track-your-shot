package com.nicholas.rutherford.track.your.shot.base.resources.declaredshotsjson

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.RawIds

class DeclaredShotsJsonImpl(private val application: Application) : DeclaredShotsJson {
    override fun fetchJsonDeclaredShots(): String {
        val inputStream = application.resources.openRawResource(RawIds.basketballShotsJson)
        return inputStream.bufferedReader().use { reader -> reader.readText() }
    }

    override fun writeToJsonDeclaredShots() {
    }
}
