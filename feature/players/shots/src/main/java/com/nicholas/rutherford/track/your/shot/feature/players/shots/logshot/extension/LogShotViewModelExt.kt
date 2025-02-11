package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import java.util.Date

interface LogShotViewModelExt {
    var logShotInfo: LogShotInfo

    fun setInitialInfo(logShotInfo: LogShotInfo)

    fun shotsAttempted(shotsMade: Int, shotsMissed: Int): Int
    fun percentageFormat(shotsMade: Double, shotsMissed: Double, isShotsMade: Boolean): String
    fun convertPercentageToDouble(percentage: String): Double
    fun convertValueToDate(value: String): Date?
    fun filterShotsById(shots: List<ShotLogged>): List<ShotLogged>

    fun invalidLogShotAlert(description: String): Alert
    fun deleteShotAlert(onYesDeleteShot: suspend () -> Unit, shotName: String): Alert
    fun deleteShotConfirmAlert(shotName: String): Alert
    fun deleteShotErrorAlert(shotName: String): Alert
    fun noChangesForShotAlert(initialShotLogged: ShotLogged?, pendingShotLogged: ShotLogged): Alert?
    fun shotEntryInvalidAlert(shotsMade: Int, shotsMissed: Int, shotsAttemptedMillisecondsValue: Long): Alert?
}
