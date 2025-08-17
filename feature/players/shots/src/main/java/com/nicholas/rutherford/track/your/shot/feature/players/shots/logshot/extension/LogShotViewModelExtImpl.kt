package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension

import android.app.Application
import android.util.Log
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.room.response.isTheSame
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseValueToDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [LogShotViewModelExt] that provides functionality for handling
 * shot logging operations such as calculating shot percentages, formatting display
 * values, filtering shots, converting data types, and creating user alerts.
 *
 * This class is a extension that should be used in the [com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotViewModel]
 *
 * @property application the application context used for fetching localized strings.
 * @property scope the coroutine scope for executing asynchronous operations.
 */
class LogShotViewModelExtImpl(
    private val application: Application,
    private val scope: CoroutineScope
) : LogShotViewModelExt {

    /** Holds the current shot log information */
    override var logShotInfo = LogShotInfo()

    /**
     * Calculates the percentage value of shots made or missed.
     *
     * @param shotsMade number of shots made as [Double].
     * @param shotsMissed number of shots missed as [Double].
     * @param isShotsMade whether to calculate percentage for made shots (`true`) or missed shots (`false`).
     * @return percentage value as [Double], or 0.0 if no shots recorded.
     */
    internal fun shotsPercentValue(
        shotsMade: Double,
        shotsMissed: Double,
        isShotsMade: Boolean
    ): Double {
        val totalShots = if (isShotsMade) {
            shotsMade + shotsMissed
        } else {
            shotsMissed + shotsMade
        }
        val percentValue =
            if (shotsMade != Constants.SHOT_ZERO_VALUE && shotsMissed != Constants.SHOT_ZERO_VALUE) {
                if (isShotsMade) {
                    shotsMade / totalShots * 100
                } else {
                    shotsMissed / totalShots * 100
                }
            } else {
                Constants.SHOT_ZERO_VALUE
            }

        return percentValue
    }

    /**
     * Sets the initial shot information.
     *
     * @param logShotInfo the [LogShotInfo] object containing shot details.
     */
    override fun setInitialInfo(logShotInfo: LogShotInfo) {
        this.logShotInfo = logShotInfo
    }

    /**
     * Calculates total shots attempted.
     *
     * @param shotsMade number of shots made.
     * @param shotsMissed number of shots missed.
     * @return total shots attempted, or 0 if both values are zero.
     */
    override fun shotsAttempted(shotsMade: Int, shotsMissed: Int): Int {
        return if (shotsMade != 0 || shotsMissed != 0) {
            shotsMade + shotsMissed
        } else {
            0
        }
    }

    /**
     * Formats shot percentage into a localized string.
     *
     * @param shotsMade number of shots made.
     * @param shotsMissed number of shots missed.
     * @param isShotsMade whether to format percentage for made shots (`true`) or missed shots (`false`).
     * @return a formatted string (e.g., "50%") or empty if no shots recorded.
     */
    override fun percentageFormat(
        shotsMade: Double,
        shotsMissed: Double,
        isShotsMade: Boolean
    ): String {
        val percentage = shotsPercentValue(
            shotsMade = shotsMade,
            shotsMissed = shotsMissed,
            isShotsMade = isShotsMade
        )

        return if (percentage == Constants.SHOT_ZERO_VALUE) {
            application.getString(StringsIds.empty)
        } else {
            val locale = Locale("en", "US")
            val percentageRoundedValue = String.format(locale, "%.1f", percentage)
            if (percentageRoundedValue.endsWith(".0")) {
                application.getString(
                    StringsIds.shotPercentage,
                    percentageRoundedValue.substring(0, percentageRoundedValue.length - 2)
                )
            } else {
                application.getString(StringsIds.shotPercentage, percentageRoundedValue)
            }
        }
    }

    /**
     * Filters out a shot from the list that matches the current shotId.
     *
     * @param shots list of [ShotLogged].
     * @return filtered list excluding the current shot.
     */
    override fun filterShotsById(shots: List<ShotLogged>): List<ShotLogged> = shots.filter { it.id != logShotInfo.shotId }

    /**
     * Converts a percentage string into a [Double].
     *
     * @param percentage string containing percentage (e.g., "45.0%").
     * @return percentage value as [Double], or 0.0 if invalid.
     */
    override fun convertPercentageToDouble(percentage: String): Double {
        if (!percentage.contains("%")) {
            return 0.0
        }

        val valueWithoutPercentSign = percentage.replace("%", "")

        val valueWithDecimal = if (!valueWithoutPercentSign.contains(".")) {
            "$valueWithoutPercentSign.0"
        } else {
            valueWithoutPercentSign
        }

        return try {
            valueWithDecimal.toDouble()
        } catch (e: NumberFormatException) {
            Log.e("NumberFormatException", "Failed to number format with - $this", e)
            0.0
        }
    }

    /**
     * Converts a string value into a [Date].
     *
     * @param value date string.
     * @return parsed [Date] or null if empty/invalid.
     */
    override fun convertValueToDate(value: String): Date? {
        return if (value.isEmpty()) {
            null
        } else {
            parseValueToDate(value = value)
        }
    }

    /**
     * Creates an alert for invalid shot logging attempts.
     *
     * @param description description of the error.
     * @return [Alert] instance.
     */
    override fun invalidLogShotAlert(description: String): Alert {
        return Alert(
            title = application.getString(StringsIds.error),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = description
        )
    }

    /**
     * Creates an alert asking for confirmation to delete a shot.
     *
     * @param onYesDeleteShot callback executed if user confirms delete.
     * @param shotName name of the shot to delete.
     * @return [Alert] instance.
     */
    override fun deleteShotAlert(onYesDeleteShot: suspend () -> Unit, shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteShot),
            description = application.getString(StringsIds.areYouSureYouWantToDeleteXShot, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onYesDeleteShot() } }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            )
        )
    }

    /**
     * Creates an alert confirming that a shot has been deleted.
     *
     * @param shotName name of the deleted shot.
     * @return [Alert] instance.
     */
    override fun deleteShotConfirmAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotHasBeenDeleted),
            description = application.getString(StringsIds.xShotHasBeenRemovedDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates an alert indicating that a shot could not be deleted.
     *
     * @param shotName name of the shot.
     * @return [Alert] instance.
     */
    override fun deleteShotErrorAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotHasNotBeenDeleted),
            description = application.getString(StringsIds.xShotHasNotBeenRemovedDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates an alert if no changes are detected between the current and pending shot.
     *
     * @param initialShotLogged original shot entry.
     * @param pendingShotLogged new shot entry to compare.
     * @return [Alert] if no changes, otherwise null.
     */
    override fun noChangesForShotAlert(initialShotLogged: ShotLogged?, pendingShotLogged: ShotLogged): Alert? {
        return initialShotLogged?.let { currentShot ->
            if (currentShot.isTheSame(pendingShotLogged)) {
                Alert(
                    title = application.getString(StringsIds.noChangesMade),
                    dismissButton = AlertConfirmAndDismissButton(
                        buttonText = application.getString(StringsIds.gotIt)
                    ),
                    description = application.getString(StringsIds.currentShotHasNoChangesDescription)
                )
            } else {
                null
            }
        } ?: run { null }
    }

    /**
     * Creates an alert if a shot entry is invalid (e.g., missing values).
     *
     * @param shotsMade number of shots made.
     * @param shotsMissed number of shots missed.
     * @param shotsAttemptedMillisecondsValue timestamp of the shot attempt.
     * @return [Alert] if invalid entry, otherwise null.
     */
    override fun shotEntryInvalidAlert(
        shotsMade: Int,
        shotsMissed: Int,
        shotsAttemptedMillisecondsValue: Long
    ): Alert? {
        val description: String? = if (shotsMade == 0) {
            application.getString(StringsIds.shotsNotRecordedDescription)
        } else if (shotsMissed == 0) {
            application.getString(StringsIds.missedShotsNotRecordedDescription)
        } else if (shotsAttemptedMillisecondsValue == 0L) {
            application.getString(StringsIds.dateShotWasTakenDescription)
        } else {
            null
        }

        return description?.let { desc ->
            Alert(
                title = application.getString(StringsIds.noChangesMade),
                dismissButton = AlertConfirmAndDismissButton(
                    buttonText = application.getString(StringsIds.gotIt)
                ),
                description = desc
            )
        } ?: run { null }
    }

    /**
     * Creates an alert for account-related problems.
     *
     * @return [Alert] instance prompting user to contact support.
     */
    override fun weHaveDetectedAProblemWithYourAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates an alert confirming a shot update.
     *
     * @return [Alert] instance.
     */
    override fun showUpdatedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotUpdated),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            ),
            description = application.getString(StringsIds.currentShotHasBeenUpdatedDescription)
        )
    }
}
