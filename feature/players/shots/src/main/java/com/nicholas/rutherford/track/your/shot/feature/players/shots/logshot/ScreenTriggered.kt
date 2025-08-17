package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

enum class ScreenTriggered {
    FROM_ALL_PLAYERS,
    FROM_FILTER_PLAYERS,
    PLAYER_WORKFLOW,
    UNKNOWN;

    companion object {
        const val INDEX_FROM_ALL_PLAYERS = 0
        const val INDEX_FROM_FILTER_PLAYERS = 1
        const val INDEX_PLAYER_WORKFLOW = 2
        const val INDEX_UNKNOWN = 3

        fun fromIndex(index: Int): ScreenTriggered =
            entries.getOrNull(index) ?: UNKNOWN
    }
}
