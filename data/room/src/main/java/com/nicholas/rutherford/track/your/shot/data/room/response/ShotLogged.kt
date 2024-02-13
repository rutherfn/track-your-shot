package com.nicholas.rutherford.track.your.shot.data.room.response

import androidx.room.ColumnInfo

data class ShotLogged(
    @ColumnInfo(name = "shotType")
    val shotType: Int,
    @ColumnInfo(name = "shotsAttempted")
    val shotsAttempted: Int,
    @ColumnInfo(name = "shotsMade")
    val shotsMade: Int,
    @ColumnInfo(name = "shotsMissed")
    val shotsMissed: Int,
    @ColumnInfo(name = "shotsMadePercentValue")
    val shotsMadePercentValue: Double,
    @ColumnInfo(name = "shotsMissedPercentValue")
    val shotsMissedPercentValue: Double,
    @ColumnInfo(name = "shotsAttemptedMillisecondsValue")
    val shotsAttemptedMillisecondsValue: Long,
    @ColumnInfo(name = "shotsLoggedMillisecondsValue")
    val shotsLoggedMillisecondsValue: Long,
    @ColumnInfo(name = "isPending")
    val isPending: Boolean
)
