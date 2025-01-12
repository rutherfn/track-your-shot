package com.nicholas.rutherford.track.your.shot.feature.reports.viewplayerreports

import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

data class ViewPlayerReportsState (
    val reports: List<IndividualPlayerReport> = emptyList()
)