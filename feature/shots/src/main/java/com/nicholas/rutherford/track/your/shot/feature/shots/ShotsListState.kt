package com.nicholas.rutherford.track.your.shot.feature.shots

data class ShotsListState(
    val shotList: List<ShotLoggedWithPlayer> = emptyList()
)
