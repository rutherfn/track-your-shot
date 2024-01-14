package com.nicholas.rutherford.track.your.shot.feature.splash

interface DeclaredShots {

    fun fetchJsonDeclaredShots(): String
    fun writeToJsonDeclaredShots()
}
