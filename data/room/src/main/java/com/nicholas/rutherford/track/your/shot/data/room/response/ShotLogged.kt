package com.nicholas.rutherford.track.your.shot.data.room.response

import androidx.room.ColumnInfo

data class ShotLogged(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "shotName")
    val shotName: String,
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

fun ShotLogged.isTheSame(shotCompared: ShotLogged): Boolean {
    return this.shotName == shotCompared.shotName &&
        this.shotType == shotCompared.shotType &&
        this.shotsAttempted == shotCompared.shotsAttempted &&
        this.shotsMade == shotCompared.shotsMade &&
        this.shotsMissed == shotCompared.shotsMissed &&
        this.shotsMadePercentValue == shotCompared.shotsMadePercentValue &&
        this.shotsMissedPercentValue == shotCompared.shotsMissedPercentValue &&
        this.shotsAttemptedMillisecondsValue == shotCompared.shotsAttemptedMillisecondsValue &&
        this.shotsLoggedMillisecondsValue == shotCompared.shotsLoggedMillisecondsValue
}
