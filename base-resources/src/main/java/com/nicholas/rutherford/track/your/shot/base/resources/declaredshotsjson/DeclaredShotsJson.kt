package com.nicholas.rutherford.track.your.shot.base.resources.declaredshotsjson

interface DeclaredShotsJson {

    fun fetchJsonDeclaredShots(): String
    fun writeToJsonDeclaredShots()
}
