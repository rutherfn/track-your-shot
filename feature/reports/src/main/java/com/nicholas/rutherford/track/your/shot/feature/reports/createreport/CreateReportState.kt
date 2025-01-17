package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import com.nicholas.rutherford.track.your.shot.data.room.response.Player

data class CreateReportState(
    val selectedPlayer: Player? = null,
    val playerOptions: List<String> = emptyList()
)
