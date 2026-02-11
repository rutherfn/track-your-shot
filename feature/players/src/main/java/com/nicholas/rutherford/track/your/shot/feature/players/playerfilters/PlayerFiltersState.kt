package com.nicholas.rutherford.track.your.shot.feature.players.playerfilters

data class PlayerFiltersState(
    val filterCount: Int = 0,
    val lastUpdatedFilterDateValue: String = "",
    val defaultPositions: List<String> = emptyList(),
    val selectedPositions: List<String> = emptyList(),
    val defaultShotLogsOptions: List<String> = emptyList(),
    val selectedShotLogsOptions: List<String> = emptyList(),
    val minShots: Int? = null,
    val maxShots: Int? = null,
    val filteredPlayerCount: Int = 0
)
