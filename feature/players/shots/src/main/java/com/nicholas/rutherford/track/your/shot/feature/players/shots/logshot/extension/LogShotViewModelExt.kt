package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import java.util.Date

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Interface defining the contract for logging shots, formatting percentages,
 * handling data conversions, filtering shots, and generating user alerts
 * within the shot logging feature.
 */
interface LogShotViewModelExt {

    /** Holds the current shot log information */
    var logShotInfo: LogShotInfo

    /**
     * Sets the initial shot information.
     *
     * @param logShotInfo the [LogShotInfo] object containing shot details.
     */
    fun setInitialInfo(logShotInfo: LogShotInfo)

    /**
     * Calculates total shots attempted.
     *
     * @param shotsMade number of shots made.
     * @param shotsMissed number of shots missed.
     * @return total shots attempted.
     */
    fun shotsAttempted(shotsMade: Int, shotsMissed: Int): Int

    /**
     * Formats shot percentage into a display string.
     *
     * @param shotsMade number of shots made.
     * @param shotsMissed number of shots missed.
     * @param isShotsMade whether to format percentage for made shots (`true`) or missed shots (`false`).
     * @return formatted percentage string.
     */
    fun percentageFormat(shotsMade: Double, shotsMissed: Double, isShotsMade: Boolean): String

    /**
     * Converts a percentage string into a [Double] value.
     *
     * @param percentage string containing percentage (e.g., "45%").
     * @return numeric percentage value as [Double].
     */
    fun convertPercentageToDouble(percentage: String): Double

    /**
     * Converts a string value into a [Date].
     *
     * @param value date string.
     * @return parsed [Date] or null if invalid.
     */
    fun convertValueToDate(value: String): Date?

    /**
     * Filters a list of shots to exclude the current shot.
     *
     * @param shots list of [ShotLogged].
     * @return filtered list of shots excluding the current shot.
     */
    fun filterShotsById(shots: List<ShotLogged>): List<ShotLogged>

    /**
     * Creates an alert for invalid shot logging attempts.
     *
     * @param description description of the error.
     * @return [Alert] instance.
     */
    fun invalidLogShotAlert(description: String): Alert

    /**
     * Creates an alert asking for confirmation to delete a shot.
     *
     * @param onYesDeleteShot callback executed if user confirms delete.
     * @param shotName name of the shot to delete.
     * @return [Alert] instance.
     */
    fun deleteShotAlert(onYesDeleteShot: suspend () -> Unit, shotName: String): Alert

    /**
     * Creates an alert confirming that a shot has been deleted.
     *
     * @param shotName name of the deleted shot.
     * @return [Alert] instance.
     */
    fun deleteShotConfirmAlert(shotName: String): Alert

    /**
     * Creates an alert indicating that a shot could not be deleted.
     *
     * @param shotName name of the shot.
     * @return [Alert] instance.
     */
    fun deleteShotErrorAlert(shotName: String): Alert

    /**
     * Creates an alert if no changes are detected between the current and pending shot.
     *
     * @param initialShotLogged original shot entry.
     * @param pendingShotLogged new shot entry to compare.
     * @return [Alert] if no changes, otherwise null.
     */
    fun noChangesForShotAlert(initialShotLogged: ShotLogged?, pendingShotLogged: ShotLogged): Alert?

    /**
     * Creates an alert if a shot entry is invalid (e.g., missing values).
     *
     * @param shotsMade number of shots made.
     * @param shotsMissed number of shots missed.
     * @param shotsAttemptedMillisecondsValue timestamp of the shot attempt.
     * @return [Alert] if invalid entry, otherwise null.
     */
    fun shotEntryInvalidAlert(shotsMade: Int, shotsMissed: Int, shotsAttemptedMillisecondsValue: Long): Alert?

    /**
     * Creates an alert for account-related problems.
     *
     * @return [Alert] instance prompting user to contact support.
     */
    fun weHaveDetectedAProblemWithYourAccountAlert(): Alert

    /**
     * Creates an alert confirming a shot update.
     *
     * @return [Alert] instance.
     */
    fun showUpdatedAlert(): Alert
}
